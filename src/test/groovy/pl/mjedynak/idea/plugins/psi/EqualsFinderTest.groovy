package pl.mjedynak.idea.plugins.psi

import com.intellij.psi.*
import com.intellij.psi.impl.source.PsiMethodImpl
import spock.lang.Specification

class EqualsFinderTest extends Specification {

    EqualsMethodFinder finder = new EqualsMethodFinder()

    PsiClass psiClass = Mock()
    PsiMethodImpl method = Mock()
    PsiType returnType = Mock()

    def setup() {
        psiClass.findMethodsByName('equals', false) >> [method]
    }

    def "should determine that given class has correct 'equals' method"() {
        psiClass.findMethodsByName('equals', false) >> [Mock(PsiMethodImpl), method]
        isPublic()
        returnsBoolean()
        hasObjectAsParameter()

        when:
        boolean result = finder.hasMethod(psiClass)

        then:
        result == true
    }

    def "should determine that method is not 'equals' if it is not public"() {
        isPrivate()
        returnsBoolean()
        hasObjectAsParameter()

        when:
        boolean result = finder.hasMethod(psiClass)

        then:
        result == false
    }

    def "should determine that method is not 'equals' if it does not return boolean"() {
        isPublic()
        returnsObject()
        hasObjectAsParameter()

        when:
        boolean result = finder.hasMethod(psiClass)

        then:
        result == false
    }

    def "should determine that method is not 'equals' if it does not have object as parameter"() {
        isPublic()
        returnsBoolean()
        hasFileAsParameter()

        when:
        boolean result = finder.hasMethod(psiClass)

        then:
        result == false
    }

    def "should determine that method is not 'equals' if it has more than one parameter"() {
        isPublic()
        returnsBoolean()
        hasMoreThanOneParameter()

        when:
        boolean result = finder.hasMethod(psiClass)

        then:
        result == false
    }

    def "should determine that method is not 'equals' if it is static"() {
        isPublic()
        isStatic()
        returnsBoolean()
        hasObjectAsParameter()

        when:
        boolean result = finder.hasMethod(psiClass)

        then:
        result == false
    }

    private void isStatic() {
        method.hasModifierProperty(PsiModifier.STATIC) >> true
    }

    private void isPublic() {
        method.hasModifierProperty(PsiModifier.PUBLIC) >> true
    }

    private void isPrivate() {
        method.hasModifierProperty(PsiModifier.PRIVATE) >> true
    }

    private void hasObjectAsParameter() {
        hasParameter(CommonClassNames.JAVA_LANG_OBJECT)
    }

    private void hasFileAsParameter() {
        hasParameter(CommonClassNames.JAVA_IO_FILE)
    }

    private void hasParameter(String parameterType) {
        PsiParameter parameter = Mock()
        parameter.type >> returnType
        returnType.canonicalText >> parameterType
        PsiParameterList psiParameterList = Mock()
        psiParameterList.parameters >> [parameter]
        method.parameterList >> psiParameterList
    }

    private void hasMoreThanOneParameter() {
        PsiParameterList psiParameterList = Mock()
        psiParameterList.parameters >> [Mock(PsiParameter), Mock(PsiParameter)]
        method.parameterList >> psiParameterList
    }

    private void returnsBoolean() {
        method.returnType >> returnType
        returnType.equalsToText('boolean') >> true
    }

    private void returnsObject() {
        method.returnType >> returnType
        returnType.equalsToText('Object') >> true
    }
}
