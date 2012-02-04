package pl.mjedynak.idea.plugins.action

import com.intellij.codeInsight.generation.ClassMember
import com.intellij.codeInsight.generation.GenerateEqualsHandler
import com.intellij.codeInsight.generation.GenerationInfo
import com.intellij.codeInsight.generation.OverrideImplementUtil
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiClass
import com.intellij.util.IncorrectOperationException
import java.lang.reflect.Field
import pl.mjedynak.idea.plugins.psi.PsiHelper

class GenerateEqualsHashCodeDeluxeActionHandler extends GenerateEqualsHandler {

    private PsiHelper psiHelper

    GenerateEqualsHashCodeDeluxeActionHandler(PsiHelper psiHelper) {
        this.psiHelper = psiHelper
    }

    @Override
    protected List<? extends GenerationInfo> generateMemberPrototypes(PsiClass psiClass, ClassMember[] originalMembers) throws IncorrectOperationException {

        def factory = JavaPsiFacade.getInstance(psiClass.project).getElementFactory()

//        def method = factory.createMethodFromText("public void generatedMethod() { }", null)
          def methodText = "public int hashCode() {return Objects.hashCode(${getFieldValue('myHashCodeFields')});}"

        def method = factory.createMethodFromText(methodText, null)
        def list = Collections.singletonList(method)
        OverrideImplementUtil.convert2GenerationInfos(list)
    }

    def getFieldValue = {  fieldName ->
        def fields = this.class.superclass.getDeclaredFields()
        Field field = fields.find { it.name == fieldName }
        field.setAccessible( true )
        return field.get( this )
    }
}
