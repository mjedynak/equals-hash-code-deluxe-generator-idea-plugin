package pl.mjedynak.idea.plugins.generator

import com.intellij.pom.java.LanguageLevel
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElementFactory
import com.intellij.psi.PsiField
import com.intellij.psi.PsiMethod
import org.jetbrains.annotations.NotNull
import pl.mjedynak.idea.plugins.psi.EqualsMethodFinder
import pl.mjedynak.idea.plugins.psi.ParentClassChecker

class EqualsGenerator {

    private ParentClassChecker parentClassChecker
    private EqualsMethodFinder equalsMethodFinder

    EqualsGenerator(ParentClassChecker parentClassChecker, EqualsMethodFinder equalsMethodFinder) {
        this.parentClassChecker = parentClassChecker
        this.equalsMethodFinder = equalsMethodFinder
    }

    PsiMethod equalsMethod(@NotNull List<PsiField> equalsPsiFields, PsiClass psiClass, String equalsMethodName) {
        if (!equalsPsiFields.isEmpty()) {
            PsiElementFactory factory = getFactory(equalsPsiFields[0])
            StringBuilder methodText = new StringBuilder()
            methodText << '@Override public boolean equals(Object obj) {'
            methodText << ' if (this == obj) {return true;}'
            methodText << ' if (obj == null || getClass() != obj.getClass()) {return false;}'
            if (parentClassChecker.hasClassWithOverriddenMethodInInheritanceHierarchy(equalsMethodFinder, psiClass)) {
                methodText << ' if (!super.equals(obj)) {return false;}'
            }
            methodText << " final ${psiClass.name} other = (${psiClass.name}) obj;"
            methodText << ' return '
            equalsPsiFields.eachWithIndex { field, index ->
                methodText <<  "Objects.${equalsMethodName}(this.${field.name}, other.${field.name})"
                if (isNotLastField(equalsPsiFields, index)) {
                    methodText << ' && '
                }
            }
            methodText << ';}'
            factory.createMethodFromText(methodText.toString(), null, LanguageLevel.JDK_1_6)
        }
    }

    private boolean isNotLastField(@NotNull List<PsiField> equalsPsiFields, int index) {
        index < equalsPsiFields.size() - 1
    }

    private PsiElementFactory getFactory(PsiField psiField) {
        JavaPsiFacade.getInstance(psiField.project).elementFactory
    }
}
