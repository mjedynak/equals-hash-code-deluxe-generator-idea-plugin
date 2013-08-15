package pl.mjedynak.idea.plugins.generator

import com.intellij.pom.java.LanguageLevel
import com.intellij.psi.*
import org.jetbrains.annotations.NotNull
import pl.mjedynak.idea.plugins.psi.ParentClassChecker

class HashCodeGenerator {

    private ParentClassChecker parentClassChecker

    HashCodeGenerator(ParentClassChecker parentClassChecker) {
        this.parentClassChecker = parentClassChecker
    }

    PsiMethod hashCodeMethod(@NotNull List<PsiField> hashCodePsiFields, PsiClass psiClass, String hashCodeMethodName) {
        if (!hashCodePsiFields.isEmpty()) {
            PsiElementFactory factory = getFactory(hashCodePsiFields[0])
            String fieldsString = hashCodePsiFields*.name.join(',')
            StringBuilder methodText = new StringBuilder()
            methodText << "@Override public int hashCode() {return "
            if (parentClassChecker.hasParentClassWithOverriddenHashCodeMethod(psiClass)) {
                methodText << '31 * super.hashCode() + '
            }
            methodText << "Objects.${hashCodeMethodName}(${fieldsString});}"
            factory.createMethodFromText(methodText.toString(), null, LanguageLevel.JDK_1_6)
        }
    }

    private PsiElementFactory getFactory(PsiField psiField) {
        JavaPsiFacade.getInstance(psiField.project).elementFactory
    }
}
