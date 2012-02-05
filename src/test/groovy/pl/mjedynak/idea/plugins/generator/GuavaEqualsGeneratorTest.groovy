package pl.mjedynak.idea.plugins.generator

import com.intellij.openapi.project.Project
import com.intellij.pom.java.LanguageLevel
import com.intellij.psi.JavaPsiFacade
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

    def setup() {
        JavaPsiFacade.metaClass.'static'.getInstance = { Project project -> javaPsiFacade}
        javaPsiFacade.getElementFactory() >> elementFactory
    }

    def "creates equals method for one field"() {
        String fieldName = 'field'
        psiField.name >> fieldName
        elementFactory.createMethodFromText("@Override public boolean equals(Object obj)        {if (obj == null) {return false}};"
      , null, LanguageLevel.JDK_1_6) >> psiMethod

        when:
        def result = hashCodeGenerator.hashCodeMethod([psiField])

        then:
        result == psiMethod
    }

    def "returns null if list is empty"() {
        when:
        def result = equalsGenerator.equalsMethod([])

        then:
        result == null
    }


}
