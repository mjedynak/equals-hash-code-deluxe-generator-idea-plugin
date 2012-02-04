package pl.mjedynak.idea.plugins.action

import com.intellij.codeInsight.generation.ClassMember
import com.intellij.codeInsight.generation.GenerateEqualsHandler
import com.intellij.codeInsight.generation.GenerationInfo
import com.intellij.codeInsight.generation.OverrideImplementUtil
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiClass
import com.intellij.util.IncorrectOperationException
import pl.mjedynak.idea.plugins.psi.PsiHelper

class GenerateEqualsHashCodeDeluxeActionHandler extends GenerateEqualsHandler {

    private PsiHelper psiHelper

    GenerateEqualsHashCodeDeluxeActionHandler(PsiHelper psiHelper) {
        this.psiHelper = psiHelper
    }

    @Override
    protected List<? extends GenerationInfo> generateMemberPrototypes(PsiClass psiClass, ClassMember[] originalMembers) throws IncorrectOperationException {

        def factory = JavaPsiFacade.getInstance(psiClass.project).getElementFactory()
        def method = factory.createMethodFromText("public void generatedMethod() { }", psiClass.allFields[0])
        def list = Collections.singletonList(method)
        OverrideImplementUtil.convert2GenerationInfos(list)
    }
}
