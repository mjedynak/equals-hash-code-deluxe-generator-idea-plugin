package pl.mjedynak.idea.plugins.psi

import com.intellij.psi.*
import com.intellij.psi.impl.source.PsiMethodImpl
import spock.lang.Specification

class EqualsFinderTest extends Specification {

    EqualsFinder finder = new EqualsFinder()

    PsiClass psiClass = Mock()
    PsiMethodImpl method = Mock()
    PsiType returnType = Mock()

    def setup() {
        psiClass.findMethodsByName("equals", false) >> [method]
    }

    def "should determine that given class has correct 'equals' method"() {
        psiClass.findMethodsByName("equals", false) >> [Mock(PsiMethodImpl), method]
        isPublic()
        returnsBoolean()
        hasObjectAsParameter()

        when:
        boolean result = finder.hasEqualsMethod(psiClass)

        then:
        result == true
    }

    def "should determine that method is not 'equals' if it is not public"() {
        isPrivate()
        returnsBoolean()
        hasObjectAsParameter()

        when:
        boolean result = finder.hasEqualsMethod(psiClass)

        then:
        result == false
    }

    def "should determine that method is not 'equals' if it does not return boolean"() {
        isPublic()
        returnsObject()
        hasObjectAsParameter()

        when:
        boolean result = finder.hasEqualsMethod(psiClass)

        then:
        result == false
    }

    def "should determine that method is not 'equals' if it does not have object as parameter"() {
        isPublic()
        returnsBoolean()
        hasFileAsParameter()

        when:
        boolean result = finder.hasEqualsMethod(psiClass)

        then:
        result == false
    }

    def "should determine that method is not 'equals' if it has more than one parameter"() {
        isPublic()
        returnsBoolean()
        hasMoreThanOneParameter()

        when:
        boolean result = finder.hasEqualsMethod(psiClass)

        then:
        result == false
    }

    def "should determine that method is not 'equals' if it is static"() {
        isPublic()
        isStatic()
        returnsBoolean()
        hasObjectAsParameter()

        when:
        boolean result = finder.hasEqualsMethod(psiClass)

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
        parameter.getType() >> returnType
        returnType.getCanonicalText() >> parameterType
        PsiParameterList psiParameterList = Mock()
        psiParameterList.getParameters() >> [parameter]
        method.getParameterList() >> psiParameterList
    }

    private void hasMoreThanOneParameter() {
        PsiParameterList psiParameterList = Mock()
        psiParameterList.getParameters() >> [Mock(PsiParameter), Mock(PsiParameter)]
        method.getParameterList() >> psiParameterList
    }

    private void returnsBoolean() {
        method.getReturnType() >> returnType
        returnType.equalsToText("boolean") >> true
    }

    private void returnsObject() {
        method.getReturnType() >> returnType
        returnType.equalsToText("Object") >> true
    }
}
