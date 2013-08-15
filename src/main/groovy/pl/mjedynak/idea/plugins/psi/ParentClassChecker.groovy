package pl.mjedynak.idea.plugins.psi

import com.intellij.psi.PsiClass

class ParentClassChecker {

    private EqualsFinder equalsFinder
    private HashCodeFinder hashCodeFinder

    ParentClassChecker(EqualsFinder equalsFinder, HashCodeFinder hashCodeFinder) {
        this.equalsFinder = equalsFinder
        this.hashCodeFinder = hashCodeFinder
    }

    boolean hasParentClassWithOverriddenEqualsMethod(PsiClass psiClass) {
        boolean result = false
        PsiClass psiParentClass = getParentClass(psiClass)
        if (psiParentClass != null) {
            result = equalsFinder.hasEqualsMethod(psiParentClass)
        }
        result
    }

    boolean hasParentClassWithOverriddenHashCodeMethod(PsiClass psiClass) {
        boolean result = false
        PsiClass psiParentClass = getParentClass(psiClass)
        if (psiParentClass != null) {
            result = hashCodeFinder.hasHashCodeMethod(psiParentClass)
        }
        result
    }

    private static PsiClass getParentClass(PsiClass psiClass) {
        PsiClass parent = null
        if (psiClass.extendsList?.referencedTypes?.length > 0) {
            parent = psiClass.extendsList.referencedTypes[0].resolve()
        }
        parent
    }
}
