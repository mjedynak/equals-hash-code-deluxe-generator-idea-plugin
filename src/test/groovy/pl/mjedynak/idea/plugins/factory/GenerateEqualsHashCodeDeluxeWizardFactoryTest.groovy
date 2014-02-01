package pl.mjedynak.idea.plugins.factory

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiClass
import pl.mjedynak.idea.plugins.model.EqualsAndHashCodeType
import pl.mjedynak.idea.plugins.wizard.GenerateEqualsHashCodeDeluxeWizard
import spock.lang.Specification

import static pl.mjedynak.idea.plugins.model.EqualsAndHashCodeType.JAVA_7

class GenerateEqualsHashCodeDeluxeWizardFactoryTest extends Specification {

    GenerateEqualsHashCodeDeluxeWizardFactory factory = new GenerateEqualsHashCodeDeluxeWizardFactory()

    GenerateEqualsHashCodeDeluxeWizard mock = Mock()

    def setup() {
        GenerateEqualsHashCodeDeluxeWizard.metaClass.constructor = { Project project, PsiClass aClass, boolean needEquals, boolean needHashCode, EqualsAndHashCodeType type -> mock }
    }

    def "creates object using constructor"() {
        when:
        GenerateEqualsHashCodeDeluxeWizard wizard = factory.createWizard(Mock(Project), Mock(PsiClass), true, true, JAVA_7)

        then:
        wizard == mock
    }
}
