package pl.mjedynak.idea.plugins.factory

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiClass
import pl.mjedynak.idea.plugins.wizard.GenerateEqualsHashCodeDeluxeWizard
import spock.lang.Specification

class GenerateEqualsHashCodeDeluxeWizardFactoryTest extends Specification {

    GenerateEqualsHashCodeDeluxeWizardFactory factory = new GenerateEqualsHashCodeDeluxeWizardFactory()

    GenerateEqualsHashCodeDeluxeWizard mock = Mock()

    def setup() {
        GenerateEqualsHashCodeDeluxeWizard.metaClass.constructor = {Project project, PsiClass aClass, boolean needEquals, boolean needHashCode -> mock }
    }

    def "creates object using constructor"() {
        when:
        GenerateEqualsHashCodeDeluxeWizard wizard = factory.createWizard(Mock(Project), Mock(PsiClass), true, true)

        then:
        wizard == mock
    }
}
