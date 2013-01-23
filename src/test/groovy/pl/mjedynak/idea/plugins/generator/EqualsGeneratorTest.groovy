package pl.mjedynak.idea.plugins.generator

import com.intellij.openapi.project.Project
import com.intellij.pom.java.LanguageLevel
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiField
import com.intellij.psi.PsiReferenceList
import com.intellij.psi.impl.PsiElementFactoryImpl
import com.intellij.psi.impl.source.PsiImmediateClassType
import com.intellij.psi.impl.source.PsiMethodImpl
import spock.lang.Specification

class EqualsGeneratorTest extends Specification {

    EqualsGenerator equalsGenerator = new EqualsGenerator()
    PsiField psiField = Mock()
    PsiField psiField2 = Mock()
    JavaPsiFacade javaPsiFacade = Mock()
    PsiElementFactoryImpl elementFactory = Mock()
    PsiMethodImpl psiMethod = Mock()
    PsiClass psiClass = Mock()
    String type = 'String'

    def setup() {
        JavaPsiFacade.metaClass.'static'.getInstance = { Project project -> javaPsiFacade}
        javaPsiFacade.elementFactory >> elementFactory
        psiClass.name >> type
        psiClass.extendsListTypes >> []
    }

    def "creates equals method for one field"() {
        String fieldName = 'field'
        psiField.name >> fieldName
        String equalsMethodName = 'equals'

        elementFactory.createMethodFromText('@Override public boolean equals(Object obj) { if (this == obj) {return true;} ' +
                'if (obj == null || getClass() != obj.getClass()) {return false;} ' +
                'final String other = (String) obj; return Objects.equals(this.field, other.field);}', null, LanguageLevel.JDK_1_6) >> psiMethod

        when:
        def result = equalsGenerator.equalsMethod([psiField], psiClass, equalsMethodName)

        then:
        result == psiMethod
    }

    def "creates equals method with super call when class extends"() {
        String fieldName = 'field'
        psiField.name >> fieldName
        String equalsMethodName = 'equals'
        PsiReferenceList psiReferenceList = Mock()
        psiReferenceList.referencedTypes >> [Mock(PsiImmediateClassType)]
        psiClass.extendsList >> psiReferenceList

        elementFactory.createMethodFromText('@Override public boolean equals(Object obj) { if (this == obj) {return true;} ' +
                'if (obj == null || getClass() != obj.getClass()) {return false;} ' +
                'if (!super.equals(obj)) {return false;} ' +
                'final String other = (String) obj; return Objects.equals(this.field, other.field);}', null, LanguageLevel.JDK_1_6) >> psiMethod

        when:
        def result = equalsGenerator.equalsMethod([psiField], psiClass, equalsMethodName)

        then:
        result == psiMethod
    }

    def "creates equals method for two fields"() {
        String fieldName = 'field'
        String fieldName2 = 'anotherField'
        psiField.name >> fieldName
        psiField2.name >> fieldName2
        String equalsMethodName = 'equal'

        elementFactory.createMethodFromText('@Override public boolean equals(Object obj) { if (this == obj) {return true;} ' +
                'if (obj == null || getClass() != obj.getClass()) {return false;} ' +
                'final String other = (String) obj; return Objects.equal(this.field, other.field) && Objects.equal(this.anotherField, other.anotherField);}',
                null, LanguageLevel.JDK_1_6) >> psiMethod

        when:
        def result = equalsGenerator.equalsMethod([psiField, psiField2], psiClass, equalsMethodName)

        then:
        result == psiMethod
    }

    def "returns null if list is empty"() {
        when:
        def result = equalsGenerator.equalsMethod([], psiClass, 'anyString')

        then:
        result == null
    }


}
