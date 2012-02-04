package pl.mjedynak.idea.plugins.psi

import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiClassOwner
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiUtilBase

class PsiHelper {

    PsiClass getPsiClassFromEditor(Editor editor, Project project) {
        PsiClass psiClass = null
        PsiFile psiFile = getPsiFile(editor, project)
        if (psiFile instanceof PsiClassOwner) {
            PsiClass[] classes = ((PsiClassOwner) psiFile).classes
            if (classes.length == 1) {
                psiClass = classes[0]
            }
        }
        psiClass
    }

    private PsiFile getPsiFile(Editor editor, Project project) {
        PsiUtilBase.getPsiFileInEditor editor, project
    }
}
