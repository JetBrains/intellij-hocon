package org.jetbrains.plugins.hocon
package usages

import com.intellij.openapi.editor.LogicalPosition
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.jetbrains.plugins.hocon.lang.HoconFileType.DefaultExtension
import org.junit.Assert.assertEquals

/**
 * @author ghik
 */
class HoconHighlightUsagesTest extends BasePlatformTestCase {

  override def getTestDataPath: String = "testdata/highlightUsages"

  def testSimple(): Unit = testUsages(
    (0, 0, 3),
    (1, 0, 3),
    (2, 0, 3),
    (3, 0, 3),
    (6, 0, 3),
    (7, 0, 3),
    (9, 12, 3)
  )

  def testNested(): Unit = testUsages(
    (4, 2, 3),
    (7, 4, 3),
    (10, 16, 3),
    (13, 19, 3),
  )

  def testInArrayElement(): Unit = testUsages(
    (6, 3, 3),
    (6, 13, 3)
  )

  def testDifferentTexts(): Unit = testUsages(
    (0, 0, 3),
    (1, 0, 5),
    (2, 0, 5),
    (3, 0, 9),
    (4, 10, 7)
  )

  def testSingle(): Unit = testUsages(
    (0, 0, 3)
  )

  private def testUsages(expectedHighlights: (Int, Int, Int)*): Unit = {
    val actualHighlights = highlights.map {
      case (startOffset, endOffset) => (logicalPositionAt(startOffset), endOffset - startOffset)
    }.map {
      case (position, length) => (position.line, position.column, length)
    }

    assertEquals(expectedHighlights.toSet, actualHighlights.toSet)
  }

  private def logicalPositionAt(offset: Int): LogicalPosition =
    myFixture.getEditor.offsetToLogicalPosition(offset)

  private def highlights: Seq[(Int, Int)] = {
    val testName = s"${getTestName(true)}.$DefaultExtension"
    myFixture.testHighlightUsages(testName).toVector.map { highlighter =>
      (highlighter.getStartOffset, highlighter.getEndOffset)
    }
  }
}
