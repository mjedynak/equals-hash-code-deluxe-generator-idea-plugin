package pl.mjedynak.idea.plugins.generator

import com.intellij.psi.PsiArrayType
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiField
import groovy.transform.CompileStatic
import org.jetbrains.annotations.NotNull
import pl.mjedynak.idea.plugins.model.EqualsAndHashCodeType
import pl.mjedynak.idea.plugins.psi.EqualsMethodFinder
import pl.mjedynak.idea.plugins.psi.ParentClassChecker

@CompileStatic
class EqualsMethodTextCreator {

    private ParentClassChecker parentClassChecker
    private EqualsMethodFinder equalsMethodFinder

    EqualsMethodTextCreator(ParentClassChecker parentClassChecker, EqualsMethodFinder equalsMethodFinder) {
        this.parentClassChecker = parentClassChecker
        this.equalsMethodFinder = equalsMethodFinder
    }

    String createMethodText(List<PsiField> equalsPsiFields, PsiClass psiClass, EqualsAndHashCodeType equalsAndHashCodeType) {
        StringBuilder methodText = new StringBuilder()
        methodText << '@Override public boolean equals(Object obj) {'
        methodText << ' if (this == obj) {return true;}'
        methodText << ' if (obj == null || getClass() != obj.getClass()) {return false;}'
        if (parentClassChecker.hasClassWithOverriddenMethodInInheritanceHierarchy(equalsMethodFinder, psiClass)) {
            methodText << ' if (!super.equals(obj)) {return false;}'
        }
        methodText << " final ${psiClass.name} other = (${psiClass.name}) obj;"
        methodText << ' return '
        equalsPsiFields.eachWithIndex { PsiField field, int index ->
            if (isArray(field)) {
                methodText << "${equalsAndHashCodeType.arrayComparisonMethodName()}(this.${field.name}, other.${field.name})"
            } else {
                methodText << "Objects.${equalsAndHashCodeType.equalsMethodName()}(this.${field.name}, other.${field.name})"
            }
            if (isNotLastField(equalsPsiFields, index)) {
                methodText << ' && '
            }
        }
        methodText << ';}'
        methodText.toString()
    }

    private boolean isArray(PsiField field) {
        field.type instanceof PsiArrayType
    }

    private boolean isNotLastField(@NotNull List<PsiField> equalsPsiFields, int index) {
        index < equalsPsiFields.size() - 1
    }
}
