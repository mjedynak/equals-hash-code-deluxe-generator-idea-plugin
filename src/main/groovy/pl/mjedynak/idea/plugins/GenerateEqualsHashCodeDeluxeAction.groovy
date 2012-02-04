package pl.mjedynak.idea.plugins

import com.intellij.openapi.editor.actionSystem.EditorAction

class GenerateEqualsHashCodeDeluxeAction extends EditorAction {

    private static GenerateEqualsHashCodeDeluxeActionHandler handler = new GenerateEqualsHashCodeDeluxeActionHandler()

    protected GenerateEqualsHashCodeDeluxeAction() {
        super(handler)
    }
}
