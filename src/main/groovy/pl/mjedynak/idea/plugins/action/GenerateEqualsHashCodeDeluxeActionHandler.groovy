package pl.mjedynak.idea.plugins.action

import com.intellij.codeInsight.generation.ClassMember
import com.intellij.codeInsight.generation.GenerateEqualsHandler
import com.intellij.codeInsight.generation.GenerationInfo
import com.intellij.codeInsight.generation.OverrideImplementUtil
import com.intellij.psi.PsiClass
import com.intellij.util.IncorrectOperationException
import java.lang.reflect.Field
import pl.mjedynak.idea.plugins.generator.GuavaHashCodeGenerator

class GenerateEqualsHashCodeDeluxeActionHandler extends GenerateEqualsHandler {

    GuavaHashCodeGenerator guavaHashCodeGenerator

    GenerateEqualsHashCodeDeluxeActionHandler(GuavaHashCodeGenerator guavaHashCodeGenerator) {
        this.guavaHashCodeGenerator = guavaHashCodeGenerator
    }


    @Override
    protected List<? extends GenerationInfo> generateMemberPrototypes(PsiClass psiClass, ClassMember[] originalMembers) throws IncorrectOperationException {

        def method = guavaHashCodeGenerator.hashCodeMethod(getFieldValue('myHashCodeFields') as List)

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
