package pl.mjedynak.idea.plugins.generator

import com.intellij.pom.java.LanguageLevel
import com.intellij.psi.*
import org.jetbrains.annotations.NotNull
import pl.mjedynak.idea.plugins.psi.HashCodeMethodFinder
import pl.mjedynak.idea.plugins.psi.ParentClassChecker

class HashCodeGenerator {

    private ParentClassChecker parentClassChecker
    private HashCodeMethodFinder hashCodeMethodFinder

    HashCodeGenerator(ParentClassChecker parentClassChecker, HashCodeMethodFinder hashCodeMethodFinder) {
        this.parentClassChecker = parentClassChecker
        this.hashCodeMethodFinder = hashCodeMethodFinder
    }

    PsiMethod hashCodeMethod(@NotNull List<PsiField> hashCodePsiFields, PsiClass psiClass, String hashCodeMethodName) {
        StringBuilder methodText = new StringBuilder()
        methodText << "@Override public int hashCode() {return "
        PsiElementFactory factory = getFactory(psiClass)
        if (!hashCodePsiFields.isEmpty()) {
            String fieldsString = hashCodePsiFields*.name.join(',')
            if (parentClassChecker.hasClassWithOverriddenMethodInInheritanceHierarchy(hashCodeMethodFinder, psiClass)) {
                methodText << '31 * super.hashCode() + '
            }
            methodText << "Objects.${hashCodeMethodName}(${fieldsString});}"
        } else {
            methodText << "0;}"
        }
        factory.createMethodFromText(methodText.toString(), null, LanguageLevel.JDK_1_6)
    }

    private PsiElementFactory getFactory(PsiClass psiClass) {
        JavaPsiFacade.getInstance(psiClass.project).elementFactory
    }
}
