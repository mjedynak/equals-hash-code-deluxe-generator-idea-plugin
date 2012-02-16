package pl.mjedynak.idea.plugins.action

import com.intellij.codeInsight.CodeInsightBundle
import com.intellij.codeInsight.generation.ClassMember
import com.intellij.codeInsight.generation.GenerateEqualsHelper
import com.intellij.codeInsight.generation.GenerateMembersHandlerBase
import com.intellij.codeInsight.generation.GenerationInfo
import com.intellij.codeInsight.generation.OverrideImplementUtil
import com.intellij.codeInsight.generation.PsiElementClassMember
import com.intellij.codeInsight.hint.HintManager
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.Messages
import com.intellij.psi.PsiAnonymousClass
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiField
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiModifier
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.util.IncorrectOperationException
import pl.mjedynak.idea.plugins.factory.GenerateEqualsHashCodeDeluxeWizardFactory
import pl.mjedynak.idea.plugins.generator.EqualsGenerator
import pl.mjedynak.idea.plugins.generator.HashCodeGenerator
import pl.mjedynak.idea.plugins.wizard.GenerateEqualsHashCodeDeluxeWizard

class GenerateEqualsHashCodeDeluxeActionHandler extends GenerateMembersHandlerBase {

    static final String METHODS_DEFINED_FOR_ANONYMOUS_CLASS = "generate.equals.and.hashcode.already.defined.warning.anonymous"
    static final String METHODS_DEFINED_FOR_CLASS = "generate.equals.and.hashcode.already.defined.warning"
    static final String TITLE = "generate.equals.and.hashcode.already.defined.title"

    static final PsiElementClassMember[] DUMMY_RESULT = new PsiElementClassMember[1] //cannot return empty array, but this result won't be used anyway

    HashCodeGenerator guavaHashCodeGenerator
    EqualsGenerator guavaEqualsGenerator
    MethodChooser methodChooser
    GenerateEqualsHashCodeDeluxeWizardFactory factory

    PsiField[] myEqualsFields = null
    PsiField[] myHashCodeFields = null


    GenerateEqualsHashCodeDeluxeActionHandler(HashCodeGenerator guavaHashCodeGenerator, EqualsGenerator guavaEqualsGenerator,
                                              MethodChooser methodChooser, GenerateEqualsHashCodeDeluxeWizardFactory factory) {
        super("")
        this.guavaHashCodeGenerator = guavaHashCodeGenerator
        this.guavaEqualsGenerator = guavaEqualsGenerator
        this.methodChooser = methodChooser
        this.factory = factory
    }

    @Override
    protected List<? extends GenerationInfo> generateMemberPrototypes(PsiClass psiClass, ClassMember[] originalMembers) throws IncorrectOperationException {

        String equalsMethodName = methodChooser.chooseEqualsMethodName(psiClass)
        String hashCodeMethodName = methodChooser.chooseHashCodeMethodName(psiClass)
        def hashCodeMethod = guavaHashCodeGenerator.hashCodeMethod(myHashCodeFields as List, hashCodeMethodName)
        def equalsMethod = guavaEqualsGenerator.equalsMethod(myEqualsFields as List, psiClass, equalsMethodName)

        OverrideImplementUtil.convert2GenerationInfos([hashCodeMethod, equalsMethod])
    }

    @Override
    protected ClassMember[] chooseOriginalMembers(PsiClass aClass, Project project, Editor editor) {
        myEqualsFields = null
        myHashCodeFields = null

        GlobalSearchScope scope = aClass.resolveScope
        final PsiMethod equalsMethod = GenerateEqualsHelper.findMethod(aClass, GenerateEqualsHelper.getEqualsSignature(project, scope))
        final PsiMethod hashCodeMethod = GenerateEqualsHelper.findMethod(aClass, GenerateEqualsHelper.hashCodeSignature)

        boolean equalsOrHashCodeExist = equalsExist(equalsMethod) || hashCodeExists(hashCodeMethod)
        boolean needEquals = !equalsExist(equalsMethod)
        boolean needHashCode = !hashCodeExists(hashCodeMethod)

        if (equalsOrHashCodeExist) {
            String text = chooseText(aClass)
            if (shouldDeleteMethods(project, text)) {
                if (methodsDeletedSuccessfully(equalsMethod, hashCodeMethod)) {
                    needEquals = needHashCode = true
                } else {
                    return null
                }
            } else {
                return null
            }
        }
        boolean hasNonStaticFields = false
        for (PsiField field: aClass.getFields()) {
            if (!field.hasModifierProperty(PsiModifier.STATIC)) {
                hasNonStaticFields = true
                break
            }
        }
        if (!hasNonStaticFields) {
            HintManager.getInstance().showErrorHint(editor, "No fields to include in equals/hashCode have been found")
            return null
        }

        GenerateEqualsHashCodeDeluxeWizard wizard = factory.createWizard(project, aClass, needEquals, needHashCode)

        wizard.show()
        if (!wizard.isOK()) {
            return null
        }
        myEqualsFields = wizard.getEqualsFields()
        myHashCodeFields = wizard.getHashCodeFields()
        return DUMMY_RESULT
    }

    private boolean methodsDeletedSuccessfully(PsiMethod equalsMethod, PsiMethod hashCodeMethod) {
        return ApplicationManager.getApplication().runWriteAction(new DeleteExistingMethodsComputable(equalsMethod, hashCodeMethod)).booleanValue()
    }

    private boolean hashCodeExists(PsiMethod hashCodeMethod) {
        hashCodeMethod != null
    }

    private boolean equalsExist(PsiMethod equalsMethod) {
        equalsMethod != null
    }

    boolean shouldDeleteMethods(Project project, String text) {
        Messages.showYesNoDialog(project, text, CodeInsightBundle.message(TITLE), Messages.getQuestionIcon()) == DialogWrapper.OK_EXIT_CODE
    }

    String chooseText(PsiClass aClass) {
        (aClass instanceof PsiAnonymousClass) ?
            CodeInsightBundle.message(METHODS_DEFINED_FOR_ANONYMOUS_CLASS) : CodeInsightBundle.message(METHODS_DEFINED_FOR_CLASS, aClass.getQualifiedName())
    }

    @Override
    protected void cleanup() {
        super.cleanup()
        myEqualsFields = null
        myHashCodeFields = null
    }

    @Override
    protected ClassMember[] getAllOriginalMembers(PsiClass psiClass) {
        null
    }

    @Override
    protected GenerationInfo[] generateMemberPrototypes(PsiClass psiClass, ClassMember classMember) {
        null
    }
}
