package pl.mjedynak.idea.plugins.generator

import com.intellij.openapi.project.Project
import com.intellij.pom.java.LanguageLevel
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiField
import com.intellij.psi.impl.PsiElementFactoryImpl
import com.intellij.psi.impl.source.PsiMethodImpl
import spock.lang.Specification

class GuavaEqualsGeneratorTest extends Specification {

    GuavaEqualsGenerator equalsGenerator = new GuavaEqualsGenerator()
    PsiField psiField = Mock()
    PsiField psiField2 = Mock()
    JavaPsiFacade javaPsiFacade = Mock()
    PsiElementFactoryImpl elementFactory = Mock()
    PsiMethodImpl psiMethod = Mock()
    PsiClass psiClass = Mock()
    String type = 'String'

    def setup() {
        JavaPsiFacade.metaClass.'static'.getInstance = { Project project -> javaPsiFacade}
        javaPsiFacade.getElementFactory() >> elementFactory
        psiClass.getName() >> type
    }

    def "creates equals method for one field"() {
        String fieldName = 'field'
        psiField.name >> fieldName

        elementFactory.createMethodFromText("@Override public boolean equals(Object obj) { if (obj == null) {return false;} " +
                "if (getClass() != obj.getClass()) {return false;} final String other = (String) obj; return Objects.equal(this.field, other.field);}", null, LanguageLevel.JDK_1_6) >> psiMethod

        when:
        def result = equalsGenerator.equalsMethod([psiField], psiClass)

        then:
        result == psiMethod
    }

    def "creates equals method for two field"() {
        String fieldName = 'field'
        String fieldName2 = 'anotherField'
        psiField.name >> fieldName
        psiField2.name >> fieldName2

        elementFactory.createMethodFromText("@Override public boolean equals(Object obj) { if (obj == null) {return false;} " +
                "if (getClass() != obj.getClass()) {return false;} " +
                "final String other = (String) obj; return Objects.equal(this.field, other.field) && Objects.equal(this.anotherField, other.anotherField);}",
                null, LanguageLevel.JDK_1_6) >> psiMethod

        when:
        def result = equalsGenerator.equalsMethod([psiField, psiField2], psiClass)

        then:
        result == psiMethod
    }

    def "returns null if list is empty"() {
        when:
        def result = equalsGenerator.equalsMethod([], psiClass)

        then:
        result == null
    }


}
