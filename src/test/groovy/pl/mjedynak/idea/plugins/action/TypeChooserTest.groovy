package pl.mjedynak.idea.plugins.action

import com.intellij.psi.PsiClass
import com.intellij.psi.util.PsiUtil
import spock.lang.Specification

import static pl.mjedynak.idea.plugins.model.EqualsAndHashCodeType.GUAVA
import static pl.mjedynak.idea.plugins.model.EqualsAndHashCodeType.JAVA_7

class TypeChooserTest extends Specification {

    TypeChooser typeChooser = new TypeChooser()
    PsiClass psiClass = Mock()

    def "chooses Java7 when language level is at least 7"() {
        PsiUtil.metaClass.'static'.isLanguageLevel7OrHigher = { PsiClass psiClass -> true }

        when:
        def result = typeChooser.chooseType(psiClass)

        then:
        result == JAVA_7
    }

    def "chooses guava  when language level is below 7"() {
        PsiUtil.metaClass.'static'.isLanguageLevel7OrHigher = { PsiClass psiClass -> false }

        when:
        def result = typeChooser.chooseType(psiClass)

        then:
        result == GUAVA
    }
}
