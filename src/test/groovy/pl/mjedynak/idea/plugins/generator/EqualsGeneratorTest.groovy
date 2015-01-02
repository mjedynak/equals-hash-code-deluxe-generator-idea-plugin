package pl.mjedynak.idea.plugins.generator

import com.intellij.openapi.project.Project
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiField
import com.intellij.psi.impl.PsiElementFactoryImpl
import com.intellij.psi.impl.source.PsiMethodImpl
import pl.mjedynak.idea.plugins.model.EqualsAndHashCodeType
import spock.lang.Specification

import static com.intellij.pom.java.LanguageLevel.JDK_1_6

class EqualsGeneratorTest extends Specification {

    EqualsMethodTextCreator equalsMethodTextCreator = Mock()
    EqualsGenerator equalsGenerator = new EqualsGenerator(equalsMethodTextCreator)
    JavaPsiFacade javaPsiFacade = Mock()
    PsiElementFactoryImpl elementFactory = Mock()
    PsiClass psiClass = Mock()
    PsiField psiField = Mock()
    PsiMethodImpl psiMethod = Mock()
    EqualsAndHashCodeType equalsAndHashCodeType = EqualsAndHashCodeType.JAVA_7

    def setup() {
        JavaPsiFacade.metaClass.static.getInstance = { Project project -> javaPsiFacade }
        javaPsiFacade.elementFactory >> elementFactory
    }

    def "creates equals method based on text"() {
        String methodText = 'methodText'
        equalsMethodTextCreator.createMethodText([psiField], psiClass, equalsAndHashCodeType) >> methodText
        elementFactory.createMethodFromText(methodText, null, JDK_1_6) >> psiMethod

        when:
        def result = equalsGenerator.equalsMethod([psiField], psiClass, equalsAndHashCodeType)

        then:
        result == psiMethod
    }

    def "returns null if psi fields list is empty"() {
        when:
        def result = equalsGenerator.equalsMethod([], psiClass, equalsAndHashCodeType)

        then:
        result == null
    }

}
