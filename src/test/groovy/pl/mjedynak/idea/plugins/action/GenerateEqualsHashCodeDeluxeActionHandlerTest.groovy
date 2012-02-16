package pl.mjedynak.idea.plugins.action

import com.intellij.codeInsight.CodeInsightBundle
import com.intellij.codeInsight.generation.GenerateEqualsHelper
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.Messages
import com.intellij.psi.PsiClass
import com.intellij.psi.impl.source.PsiMethodImpl
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.MethodSignature
import javax.swing.Icon
import pl.mjedynak.idea.plugins.factory.GenerateEqualsHashCodeDeluxeWizardFactory
import pl.mjedynak.idea.plugins.generator.EqualsGenerator
import pl.mjedynak.idea.plugins.generator.HashCodeGenerator
import spock.lang.Specification

class GenerateEqualsHashCodeDeluxeActionHandlerTest extends Specification {

    private static final int OK_EXIT_CODE = DialogWrapper.OK_EXIT_CODE
    private static final int NOT_OK_EXIT_CODE = DialogWrapper.OK_EXIT_CODE + 1


    GenerateEqualsHashCodeDeluxeActionHandler actionHandler =
        new GenerateEqualsHashCodeDeluxeActionHandler(guavaHashCodeGenerator, guavaEqualsGenerator, methodChooser, factory)

    HashCodeGenerator guavaHashCodeGenerator = Mock()
    EqualsGenerator guavaEqualsGenerator = Mock()
    MethodChooser methodChooser = Mock()
    GenerateEqualsHashCodeDeluxeWizardFactory factory = Mock()

    PsiClass psiClass = Mock()
    Project project = Mock()
    Editor editor = Mock()
    MethodSignature equalsMethodSignature = Mock()
    MethodSignature hashCodeMethodSignature = Mock()
    PsiMethodImpl equalsMethod = Mock()
    PsiMethodImpl hashCodeMethod = Mock()

    def setup() {
        GenerateEqualsHelper.metaClass.'static'.getEqualsSignature = { Project project, GlobalSearchScope scope -> equalsMethodSignature }
        GenerateEqualsHelper.metaClass.'static'.getHashCodeSignature = { hashCodeMethodSignature }
        GenerateEqualsHelper.metaClass.'static'.findMethod = {PsiClass psiClass, MethodSignature methodSignature ->
            if (methodSignature == equalsMethodSignature) {
                equalsMethod
            } else {
                hashCodeMethod
            }
        }
        CodeInsightBundle.metaClass.'static'.message = {String key -> "anyString"}
        Messages.metaClass.'static'.getQuestionIcon = {Mock(Icon)}
        Messages.metaClass.'static'.showYesNoDialog = {Project project, String message, String title, Icon icon -> NOT_OK_EXIT_CODE}
    }

    def "does not display wizard and do anything else when methods exist and user decides not to delete them"() {
        when:
        def members = actionHandler.chooseOriginalMembers(psiClass, project, editor)

        then:
        members == null
        1 * psiClass.getResolveScope()
        0 * _
    }
}
