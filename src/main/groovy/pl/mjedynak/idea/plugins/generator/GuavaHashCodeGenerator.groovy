package pl.mjedynak.idea.plugins.generator

import com.intellij.pom.java.LanguageLevel
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiElementFactory
import com.intellij.psi.PsiField
import com.intellij.psi.PsiMethod
import org.jetbrains.annotations.NotNull

class GuavaHashCodeGenerator {

    PsiMethod hashCodeMethod(@NotNull List<PsiField> hashCodePsiFields) {
        if (!hashCodePsiFields.isEmpty()) {
            PsiElementFactory factory = getFactory(hashCodePsiFields[0])
            def fieldsString = hashCodePsiFields.collect {it.name}.join(",")
            def methodText = "@Override public int hashCode() {return Objects.hashCode(${fieldsString});}"
            factory.createMethodFromText(methodText, null, LanguageLevel.JDK_1_6)
        }
    }

    private PsiElementFactory getFactory(PsiField psiField) {
        return JavaPsiFacade.getInstance(psiField.project).getElementFactory()
    }
}
