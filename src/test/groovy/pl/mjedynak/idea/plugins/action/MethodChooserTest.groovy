package pl.mjedynak.idea.plugins.action

import com.intellij.psi.PsiClass
import com.intellij.psi.util.PsiUtil
import spock.lang.Specification

class MethodChooserTest extends Specification {
    
    MethodChooser methodChooser = new MethodChooser()
    PsiClass psiClass = Mock()

    def "chooses Java7 equals method when language level is at least 7"() {
        PsiUtil.metaClass.'static'.isLanguageLevel7OrHigher = {PsiClass psiClass -> true}

        when:
        def result = methodChooser.chooseEqualsMethodName(psiClass)

        then:
        result == MethodChooser.JAVA_7_EQUALS_METHOD
    }

    def "chooses guava equals method when language level is less than 7"() {
        PsiUtil.metaClass.'static'.isLanguageLevel7OrHigher = {PsiClass psiClass -> false}

        when:
        def result = methodChooser.chooseEqualsMethodName(psiClass)

        then:
        result == MethodChooser.GUAVA_EQUALS_METHOD
    }

    def "chooses Java7 hashCode method when language level is at least 7"() {
        PsiUtil.metaClass.'static'.isLanguageLevel7OrHigher = {PsiClass psiClass -> true}

        when:
        def result = methodChooser.chooseHashCodeMethodName(psiClass)

        then:
        result == MethodChooser.JAVA_7_HASH_CODE_METHOD
    }

    def "chooses Java7 hashCode method when language level is below 7"() {
        PsiUtil.metaClass.'static'.isLanguageLevel7OrHigher = {PsiClass psiClass -> false}

        when:
        def result = methodChooser.chooseHashCodeMethodName(psiClass)

        then:
        result == MethodChooser.GUAVA_HASH_CODE_METHOD
    }
}
