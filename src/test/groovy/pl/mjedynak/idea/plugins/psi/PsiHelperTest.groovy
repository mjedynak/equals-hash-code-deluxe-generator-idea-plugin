package pl.mjedynak.idea.plugins.psi

import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiClassOwner
import com.intellij.psi.util.PsiUtilBase
import spock.lang.Specification

class PsiHelperTest extends Specification {

    PsiHelper helper = new PsiHelper()
    Editor editor = Mock()
    Project project = Mock()
    PsiClassOwner psiClassOwner = Mock()
    PsiClass psiClass = Mock()

    def setup() {
        PsiUtilBase.metaClass.'static'.getPsiFileInEditor = { Editor editor, Project project -> psiClassOwner}
    }

    def "retrieves PsiClass if PsiFile in editor is instance of PsiClassOwner"() {
        psiClassOwner.classes >> [psiClass]

        when:
        def result = helper.getPsiClassFromEditor(editor, project)

        then:
        result == psiClass
    }
}
