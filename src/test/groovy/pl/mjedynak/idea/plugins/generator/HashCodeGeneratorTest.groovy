package pl.mjedynak.idea.plugins.generator

import com.intellij.openapi.project.Project
import com.intellij.pom.java.LanguageLevel
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiField
import com.intellij.psi.impl.PsiElementFactoryImpl
import com.intellij.psi.impl.source.PsiMethodImpl
import spock.lang.Specification

class HashCodeGeneratorTest extends Specification {

    def HashCodeGenerator hashCodeGenerator = new HashCodeGenerator()
    PsiField psiField = Mock()
    JavaPsiFacade javaPsiFacade = Mock()
    PsiElementFactoryImpl elementFactory = Mock()
    PsiMethodImpl psiMethod = Mock()

//    class MockablePsiElementFactory extends PsiElementFactory {
//
//    }


    def setup() {
        JavaPsiFacade.metaClass.'static'.getInstance = { Project project -> javaPsiFacade}
        javaPsiFacade.getElementFactory() >> elementFactory
    }

    def "creates hashCode method for one field"() {
        String fieldName = 'field'
        psiField.name >> fieldName
//        elementFactory.createMethodFromText("public int hashCode() { return Objects.hashCode(field); }", null, _ as LanguageLevel) >> psiMethod
        elementFactory.createMethodFromText(_ as String,null, _ as LanguageLevel) >> psiMethod


        when:
        def result = hashCodeGenerator.hashCodeMethod([psiField])

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
