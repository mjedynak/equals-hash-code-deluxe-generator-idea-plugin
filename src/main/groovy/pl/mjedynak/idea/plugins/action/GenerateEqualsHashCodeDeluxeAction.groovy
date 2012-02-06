package pl.mjedynak.idea.plugins.action

import com.intellij.codeInsight.generation.actions.BaseGenerateAction
import org.picocontainer.MutablePicoContainer
import org.picocontainer.defaults.DefaultPicoContainer
import pl.mjedynak.idea.plugins.generator.GuavaHashCodeGenerator
import pl.mjedynak.idea.plugins.generator.GuavaEqualsGenerator

class GenerateEqualsHashCodeDeluxeAction extends BaseGenerateAction {

    private static MutablePicoContainer picoContainer = new DefaultPicoContainer()

    private static GenerateEqualsHashCodeDeluxeActionHandler handler

    static {
        picoContainer.registerComponentImplementation(GuavaHashCodeGenerator)
        picoContainer.registerComponentImplementation(GuavaEqualsGenerator)
        picoContainer.registerComponentImplementation(GenerateEqualsHashCodeDeluxeActionHandler)
        handler = picoContainer.getComponentInstanceOfType(GenerateEqualsHashCodeDeluxeActionHandler)
    }


    protected GenerateEqualsHashCodeDeluxeAction() {
        super(handler)
    }
}
