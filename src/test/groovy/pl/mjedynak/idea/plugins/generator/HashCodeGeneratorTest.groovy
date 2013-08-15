package pl.mjedynak.idea.plugins.generator

import com.intellij.openapi.project.Project
import com.intellij.pom.java.LanguageLevel
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiField
import com.intellij.psi.impl.PsiElementFactoryImpl
import com.intellij.psi.impl.source.PsiMethodImpl
import pl.mjedynak.idea.plugins.psi.ParentClassChecker
import spock.lang.Specification

class HashCodeGeneratorTest extends Specification {

    ParentClassChecker parentClassChecker = Mock()
    HashCodeGenerator hashCodeGenerator = new HashCodeGenerator(parentClassChecker)
    PsiClass psiClass = Mock()
    PsiField psiField = Mock()
    PsiField psiField2 = Mock()
    JavaPsiFacade javaPsiFacade = Mock()
    PsiElementFactoryImpl elementFactory = Mock()
    PsiMethodImpl psiMethod = Mock()

    def setup() {
        JavaPsiFacade.metaClass.'static'.getInstance = { Project project -> javaPsiFacade}
        javaPsiFacade.elementFactory >> elementFactory
    }

    def "creates hashCode method for one field"() {
        String fieldName = 'field'
        psiField.name >> fieldName
        String hashCodeMethodName = 'hash'
        elementFactory.createMethodFromText('@Override public int hashCode() {return Objects.hash(field);}', null, LanguageLevel.JDK_1_6) >> psiMethod

        when:
        def result = hashCodeGenerator.hashCodeMethod([psiField], psiClass, hashCodeMethodName)

        then:
        result == psiMethod
    }

    def "creates hashCode method for two fields"() {
        String fieldName = 'field'
        String field2Name = 'anotherField'
        psiField.name >> fieldName
        psiField2.name >> field2Name
        String hashCodeMethodName = 'hashCode'
        elementFactory.createMethodFromText('@Override public int hashCode() {return Objects.hashCode(field,anotherField);}', null, LanguageLevel.JDK_1_6) >> psiMethod

        when:
        def result = hashCodeGenerator.hashCodeMethod([psiField, psiField2], psiClass, hashCodeMethodName)

        then:
        result == psiMethod
    }

    def "creates hashCode method with super call when parent class checker says so"() {
        parentClassChecker.hasParentClassWithOverriddenHashCodeMethod(psiClass) >> true
        String fieldName = 'field'
        psiField.name >> fieldName
        String hashCodeMethodName = 'hash'
        elementFactory.createMethodFromText('@Override public int hashCode() {return 31 * super.hashCode() + Objects.hash(field);}', null, LanguageLevel.JDK_1_6) >> psiMethod

        when:
        def result = hashCodeGenerator.hashCodeMethod([psiField], psiClass, hashCodeMethodName)

        then:
        result == psiMethod
    }

    def "returns null if list is empty"() {
        when:
        def result = hashCodeGenerator.hashCodeMethod([], psiClass, 'anyString')

        then:
        result == null
    }
}
