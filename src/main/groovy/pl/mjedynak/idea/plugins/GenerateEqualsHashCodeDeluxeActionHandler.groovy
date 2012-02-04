package pl.mjedynak.idea.plugins

import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.actionSystem.DataKeys
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.actionSystem.EditorActionHandler
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiClass
import pl.mjedynak.idea.plugins.psi.PsiHelper

class GenerateEqualsHashCodeDeluxeActionHandler extends EditorActionHandler {

    private PsiHelper psiHelper

    GenerateEqualsHashCodeDeluxeActionHandler(PsiHelper psiHelper) {
        this.psiHelper = psiHelper
    }



    @Override
    void execute(Editor editor, DataContext dataContext) {
        Project project = dataContext.getData(DataKeys.PROJECT.getName());
        PsiClass psiClass = psiHelper.getPsiClassFromEditor(editor, project)
        println psiClass.getAllFields()

    }
}
