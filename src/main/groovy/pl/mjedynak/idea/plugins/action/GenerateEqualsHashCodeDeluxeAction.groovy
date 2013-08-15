package pl.mjedynak.idea.plugins.action

import com.intellij.codeInsight.generation.actions.BaseGenerateAction
import org.picocontainer.MutablePicoContainer
import org.picocontainer.defaults.DefaultPicoContainer
import pl.mjedynak.idea.plugins.factory.GenerateEqualsHashCodeDeluxeWizardFactory
import pl.mjedynak.idea.plugins.generator.EqualsGenerator
import pl.mjedynak.idea.plugins.generator.HashCodeGenerator
import pl.mjedynak.idea.plugins.psi.EqualsFinder
import pl.mjedynak.idea.plugins.psi.HashCodeFinder
import pl.mjedynak.idea.plugins.psi.ParentClassChecker

class GenerateEqualsHashCodeDeluxeAction extends BaseGenerateAction {

    private static MutablePicoContainer picoContainer = new DefaultPicoContainer()

    private static GenerateEqualsHashCodeDeluxeActionHandler handler

    static {
        picoContainer.registerComponentImplementation(EqualsFinder)
        picoContainer.registerComponentImplementation(HashCodeFinder)
        picoContainer.registerComponentImplementation(ParentClassChecker)
        picoContainer.registerComponentImplementation(HashCodeGenerator)
        picoContainer.registerComponentImplementation(EqualsGenerator)
        picoContainer.registerComponentImplementation(TypeChooser)
        picoContainer.registerComponentImplementation(GenerateEqualsHashCodeDeluxeWizardFactory)
        picoContainer.registerComponentImplementation(GenerateEqualsHashCodeDeluxeActionHandler)
        handler = picoContainer.getComponentInstanceOfType(GenerateEqualsHashCodeDeluxeActionHandler.class)
    }


    protected GenerateEqualsHashCodeDeluxeAction() {
        super(handler)
    }
}
