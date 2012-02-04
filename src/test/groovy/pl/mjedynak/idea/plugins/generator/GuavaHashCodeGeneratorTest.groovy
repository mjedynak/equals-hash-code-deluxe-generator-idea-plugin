package pl.mjedynak.idea.plugins.generator

import com.intellij.openapi.project.Project
import com.intellij.pom.java.LanguageLevel
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiField
import com.intellij.psi.impl.PsiElementFactoryImpl
import com.intellij.psi.impl.source.PsiMethodImpl
import spock.lang.Specification

class GuavaHashCodeGeneratorTest extends Specification {

    def GuavaHashCodeGenerator hashCodeGenerator = new GuavaHashCodeGenerator()
    PsiField psiField = Mock()
    PsiField psiField2 = Mock()
    JavaPsiFacade javaPsiFacade = Mock()
    PsiElementFactoryImpl elementFactory = Mock()
    PsiMethodImpl psiMethod = Mock()

    def setup() {
        JavaPsiFacade.metaClass.'static'.getInstance = { Project project -> javaPsiFacade}
        javaPsiFacade.getElementFactory() >> elementFactory
    }

    def "creates hashCode method for one field"() {
        String fieldName = 'field'
        psiField.name >> fieldName
        elementFactory.createMethodFromText("public int hashCode() {return Objects.hashCode(field);}", null, LanguageLevel.JDK_1_6) >> psiMethod

        when:
        def result = hashCodeGenerator.hashCodeMethod([psiField])

        then:
        result == psiMethod
    }

    def "creates hashCode method for two fields"() {
        String fieldName = 'field'
        String field2Name = 'anotherField'
        psiField.name >> fieldName
        psiField2.name >> field2Name
        elementFactory.createMethodFromText("public int hashCode() {return Objects.hashCode(field,anotherField);}", null, LanguageLevel.JDK_1_6) >> psiMethod

        when:
        def result = hashCodeGenerator.hashCodeMethod([psiField, psiField2])

        then:
        result == psiMethod
    }

    def "returns null if list is empty"() {
        when:
        def result = hashCodeGenerator.hashCodeMethod([])

        then:
        result == null
    }
}
