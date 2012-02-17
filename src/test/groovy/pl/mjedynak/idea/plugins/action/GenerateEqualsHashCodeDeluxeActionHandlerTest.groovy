package pl.mjedynak.idea.plugins.action

import com.intellij.codeInsight.CodeInsightBundle
import com.intellij.codeInsight.generation.ClassMember
import com.intellij.codeInsight.generation.GenerateEqualsHelper
import com.intellij.codeInsight.hint.HintManager
import com.intellij.openapi.application.Application
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.Messages
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiField
import com.intellij.psi.PsiModifier
import com.intellij.psi.impl.source.PsiMethodImpl
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.MethodSignature
import javax.swing.Icon
import pl.mjedynak.idea.plugins.factory.GenerateEqualsHashCodeDeluxeWizardFactory
import pl.mjedynak.idea.plugins.generator.EqualsGenerator
import pl.mjedynak.idea.plugins.generator.HashCodeGenerator
import pl.mjedynak.idea.plugins.wizard.GenerateEqualsHashCodeDeluxeWizard
import spock.lang.Specification

class GenerateEqualsHashCodeDeluxeActionHandlerTest extends Specification {

    private static final int OK_EXIT_CODE = DialogWrapper.OK_EXIT_CODE
    private static final int NOT_OK_EXIT_CODE = DialogWrapper.OK_EXIT_CODE + 1

    GenerateEqualsHashCodeDeluxeActionHandler actionHandler

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
    Application application = Mock()
    HintManager hintManager = Mock()
    GenerateEqualsHashCodeDeluxeWizard wizard = Mock()

    ClassMember[] result


    def setup() {
        actionHandler = new GenerateEqualsHashCodeDeluxeActionHandler(guavaHashCodeGenerator, guavaEqualsGenerator, methodChooser, factory)

        GenerateEqualsHelper.metaClass.'static'.getEqualsSignature = { Project project, GlobalSearchScope scope -> equalsMethodSignature }
        GenerateEqualsHelper.metaClass.'static'.getHashCodeSignature = { hashCodeMethodSignature }
        GenerateEqualsHelper.metaClass.'static'.findMethod = {PsiClass psiClass, MethodSignature methodSignature -> null}
        CodeInsightBundle.metaClass.'static'.message = {String key -> "anyString"}
        Messages.metaClass.'static'.getQuestionIcon = {Mock(Icon)}
        ApplicationManager.metaClass.'static'.getApplication = {application}
        HintManager.metaClass.'static'.getInstance = {hintManager}
        factory.createWizard(project, psiClass, true, true) >> wizard
    }

    def "does not display wizard when methods exist and user decides not to delete them"() {
        equalsAndHashCodeExist()
        userClicksNoInDeleteDialog()

        when:
        result = actionHandler.chooseOriginalMembers(psiClass, project, editor)

        then:
        methodsNotCreated()
        0 * factory._
    }


    def "does not display wizard when methods exist but deletion is not successful"() {
        equalsAndHashCodeExist()
        userClicksYesInDeleteDialog()
        deletionNotSuccessful()

        when:
        result = actionHandler.chooseOriginalMembers(psiClass, project, editor)

        then:
        methodsNotCreated()
        0 * factory._
    }

    def "displays error message when class has only static fields"() {
        equalsAndHashCodeExist()
        userClicksYesInDeleteDialog()
        deletionSuccessful()
        classHasOnlyStaticFields()

        when:
        result = actionHandler.chooseOriginalMembers(psiClass, project, editor)

        then:
        methodsNotCreated()
        1 * hintManager.showErrorHint(editor, actionHandler.ONLY_STATIC_FIELDS_ERROR)
        0 * factory._
    }

    def "shows wizard but does not create methods because user clicks cancel"() {
        classHasNoStaticField()
        userClicksCancelInWizard()

        when:
        result = actionHandler.chooseOriginalMembers(psiClass, project, editor)

        then:
        methodsNotCreated()
        1 * wizard.show()
    }

    def "creates methods"() {
        classHasNoStaticField()
        userClicksOkInWizard()

        when:
        result = actionHandler.chooseOriginalMembers(psiClass, project, editor)

        then:
        methodsCreated()
        1 * wizard.show()
    }

    def userClicksOkInWizard() {
        wizard.isOK() >> true
    }

    def userClicksCancelInWizard() {
        wizard.isOK() >> false
    }

    def classHasNoStaticField() {
        PsiField field = Mock()
        field.hasModifierProperty(PsiModifier.STATIC) >> false
        PsiField[] fields = [field]
        psiClass.fields >> fields
    }

    def classHasOnlyStaticFields() {
        PsiField field = Mock()
        field.hasModifierProperty(PsiModifier.STATIC) >> true
        PsiField[] fields = [field]
        psiClass.fields >> fields
    }

    def deletionNotSuccessful() {
        application.runWriteAction(_ as DeleteExistingMethodsComputable) >> false
    }

    def deletionSuccessful() {
        application.runWriteAction(_ as DeleteExistingMethodsComputable) >> true
    }

    def userClicksYesInDeleteDialog() {
        Messages.metaClass.'static'.showYesNoDialog = {Project project, String message, String title, Icon icon -> OK_EXIT_CODE}
    }

    def userClicksNoInDeleteDialog() {
        Messages.metaClass.'static'.showYesNoDialog = {Project project, String message, String title, Icon icon -> NOT_OK_EXIT_CODE}
    }

    def equalsAndHashCodeExist() {
        GenerateEqualsHelper.metaClass.'static'.findMethod = {PsiClass psiClass, MethodSignature methodSignature ->
            if (methodSignature == equalsMethodSignature) {
                equalsMethod
            } else {
                hashCodeMethod
            }
        }
    }

    def boolean methodsNotCreated() {
        result == null
    }

    def boolean methodsCreated() {
        result != null
    }

}
