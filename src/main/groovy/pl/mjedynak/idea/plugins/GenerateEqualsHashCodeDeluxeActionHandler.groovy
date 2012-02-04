package pl.mjedynak.idea.plugins

import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.actionSystem.EditorActionHandler

class GenerateEqualsHashCodeDeluxeActionHandler extends EditorActionHandler {

    @Override
    void execute(Editor editor, DataContext dataContext) {
        println "aa"
    }
}
