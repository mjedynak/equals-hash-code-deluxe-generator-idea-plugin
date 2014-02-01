package pl.mjedynak.idea.plugins.psi

import com.intellij.psi.PsiClass
import com.intellij.psi.PsiClassType
import com.intellij.psi.PsiReferenceList
import spock.lang.Specification

class ParentClassCheckerTest extends Specification {

    MethodFinder methodFinder = Mock()
    PsiClass psiClass = Mock()
    PsiClass parentPsiClass = Mock()
    ParentClassChecker checker = new ParentClassChecker()

    def "should determine that given class has class with overridden method in its inheritance hierarchy"() {
        PsiReferenceList extendsList = Mock()
        psiClass.extendsList >> extendsList
        PsiClassType psiClassType = Mock()
        extendsList.referencedTypes >> [psiClassType]
        psiClassType.resolve() >> parentPsiClass
        methodFinder.hasMethod(parentPsiClass) >> false
        PsiReferenceList parentExtendsList = Mock()
        parentPsiClass.extendsList >> parentExtendsList
        PsiClassType parentPsiClassType = Mock()
        parentExtendsList.referencedTypes >> [parentPsiClassType]
        PsiClass grandParentPsiClass = Mock()
        parentPsiClassType.resolve() >> grandParentPsiClass
        methodFinder.hasMethod(grandParentPsiClass) >> true

        when:
        boolean result = checker.hasClassWithOverriddenMethodInInheritanceHierarchy(methodFinder, psiClass)

        then:
        result == true
    }

    def "should determine that given class has parent class with overridden method"() {
        PsiReferenceList extendsList = Mock()
        psiClass.extendsList >> extendsList
        PsiClassType psiClassType = Mock()
        extendsList.referencedTypes >> [psiClassType]
        psiClassType.resolve() >> parentPsiClass
        methodFinder.hasMethod(parentPsiClass) >> true

        when:
        boolean result = checker.hasClassWithOverriddenMethodInInheritanceHierarchy(methodFinder, psiClass)

        then:
        result == true
    }

    def "should return false if there is no parent class"() {
        PsiReferenceList extendsList = Mock()
        psiClass.extendsList >> extendsList
        extendsList.referencedTypes >> []

        when:
        boolean result = checker.hasClassWithOverriddenMethodInInheritanceHierarchy(methodFinder, psiClass)

        then:
        result == false
    }

    def "should return false if finder does not find correct method in parent"() {
        PsiReferenceList extendsList = Mock()
        psiClass.extendsList >> extendsList
        PsiClassType psiClassType = Mock()
        extendsList.referencedTypes >> [psiClassType]
        psiClassType.resolve() >> parentPsiClass
        methodFinder.hasMethod(parentPsiClass) >> false
        PsiReferenceList parentExtendsList = Mock()
        parentPsiClass.extendsList >> parentExtendsList
        parentExtendsList.referencedTypes >> []

        when:
        boolean result = checker.hasClassWithOverriddenMethodInInheritanceHierarchy(methodFinder, psiClass)

        then:
        result == false
    }
}
