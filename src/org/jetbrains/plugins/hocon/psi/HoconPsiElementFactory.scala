package org.jetbrains.plugins.hocon
package psi

import com.intellij.psi.{PsiFileFactory, PsiManager}
import org.jetbrains.plugins.hocon.lang.HoconFileType

import scala.reflect.ClassTag

object HoconPsiElementFactory {
  private val Dummy = "dummy."

  private def createElement[T <: HoconPsiElement : ClassTag](manager: PsiManager, text: String, offset: Int): Option[T] = {
    val element = PsiFileFactory.getInstance(manager.getProject)
      .createFileFromText(Dummy + HoconFileType.DefaultExtension, new HoconFileType, text).findElementAt(offset)
    Iterator.iterate(element)(_.getParent).takeWhile(_ != null).collectFirst({ case t: T => t })
  }

  def createStringValue(contents: String, manager: PsiManager): HStringValue =
    createElement[HStringValue](manager, s"__k = $contents", 6).orNull

  def createKeyPart(contents: String, manager: PsiManager): HKeyPart =
    createElement[HKeyPart](manager, s"$contents = null", 0).orNull

  def createIncludeTarget(contents: String, manager: PsiManager): HIncludeTarget =
    createElement[HIncludeTarget](manager, s"include $contents", 8).orNull

  def createFieldKey(contents: String, manager: PsiManager): HFieldKey =
    createElement[HFieldKey](manager, s"$contents = null", 0).orNull

  def createSubstitutionKey(contents: String, manager: PsiManager): HSubstitutionKey =
    createElement[HSubstitutionKey](manager, s"__k = $${$contents}}", 8).orNull

  def createPath(path: String, manager: PsiManager): HPath =
    createElement[HSubstitution](manager, s"__k = $${$path}", 8).flatMap(_.path).orNull
}
