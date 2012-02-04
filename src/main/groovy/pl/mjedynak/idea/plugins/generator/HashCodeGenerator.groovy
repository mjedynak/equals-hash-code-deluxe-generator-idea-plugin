package pl.mjedynak.idea.plugins.generator

import com.intellij.pom.java.LanguageLevel
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiField
import com.intellij.psi.PsiMethod
import com.intellij.psi.impl.PsiElementFactoryImpl
import org.jetbrains.annotations.NotNull

class HashCodeGenerator {

    PsiMethod hashCodeMethod(@NotNull  List<PsiField> hashCodePsiFields) {
        if (!hashCodePsiFields.isEmpty()) {
            PsiElementFactoryImpl factory = getFactory(hashCodePsiFields[0])
            def methodText = "public int hashCode() {return Objects.hashCode(${hashCodePsiFields});}"

            factory.createMethodFromText(methodText, null, LanguageLevel.JDK_1_6)

        }
    }

    private PsiElementFactoryImpl getFactory(PsiField psiField) {
        return JavaPsiFacade.getInstance(psiField.project).getElementFactory()
    }
}
