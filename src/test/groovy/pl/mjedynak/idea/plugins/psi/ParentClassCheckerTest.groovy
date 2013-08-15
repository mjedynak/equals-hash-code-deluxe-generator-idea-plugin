package pl.mjedynak.idea.plugins.psi

import com.intellij.psi.PsiClass
import com.intellij.psi.PsiClassType
import com.intellij.psi.PsiReferenceList
import spock.lang.Specification

class ParentClassCheckerTest extends Specification {

    EqualsFinder equalsFinder = Mock()
    HashCodeFinder hashCodeFinder = Mock()
    PsiClass psiClass = Mock()
    PsiClass parentPsiClass = Mock()
    ParentClassChecker checker = new ParentClassChecker(equalsFinder, hashCodeFinder)

    def "should determine that given class has parent class with overridden equals method"() {
        PsiReferenceList extendsList = Mock()
        psiClass.extendsList >> extendsList
        PsiClassType psiClassType = Mock()
        extendsList.referencedTypes >> [psiClassType]
        psiClassType.resolve() >> parentPsiClass
        equalsFinder.hasEqualsMethod(parentPsiClass) >> true

        when:
        boolean result = checker.hasParentClassWithOverriddenEqualsMethod(psiClass)

        then:
        result == true
    }

    def "should return false for 'equals' if there is no parent class"() {
        PsiReferenceList extendsList = Mock()
        psiClass.extendsList >> extendsList
        extendsList.referencedTypes >> []

        when:
        boolean result = checker.hasParentClassWithOverriddenEqualsMethod(psiClass)

        then:
        result == false
    }

    def "should return false if finder does not find correct 'equals' in parent"() {
        PsiReferenceList extendsList = Mock()
        psiClass.extendsList >> extendsList
        PsiClassType psiClassType = Mock()
        extendsList.referencedTypes >> [psiClassType]
        psiClassType.resolve() >> parentPsiClass
        equalsFinder.hasEqualsMethod(parentPsiClass) >> false

        when:
        boolean result = checker.hasParentClassWithOverriddenEqualsMethod(psiClass)

        then:
        result == false
    }

    def "should determine that given class has parent class with overridden hashCode method"() {
        PsiReferenceList extendsList = Mock()
        psiClass.extendsList >> extendsList
        PsiClassType psiClassType = Mock()
        extendsList.referencedTypes >> [psiClassType]
        psiClassType.resolve() >> parentPsiClass
        hashCodeFinder.hasHashCodeMethod(parentPsiClass) >> true

        when:
        boolean result = checker.hasParentClassWithOverriddenHashCodeMethod(psiClass)

        then:
        result == true
    }

    def "should return false for 'hash code' method if there is no parent class"() {
        PsiReferenceList extendsList = Mock()
        psiClass.extendsList >> extendsList
        extendsList.referencedTypes >> []

        when:
        boolean result = checker.hasParentClassWithOverriddenHashCodeMethod(psiClass)

        then:
        result == false
    }
}
