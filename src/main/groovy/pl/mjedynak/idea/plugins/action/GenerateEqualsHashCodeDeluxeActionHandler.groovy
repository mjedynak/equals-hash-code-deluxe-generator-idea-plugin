package pl.mjedynak.idea.plugins.action

import com.intellij.codeInsight.generation.ClassMember
import com.intellij.codeInsight.generation.GenerateEqualsHandler
import com.intellij.codeInsight.generation.GenerationInfo
import com.intellij.codeInsight.generation.OverrideImplementUtil
import com.intellij.psi.PsiClass
import com.intellij.util.IncorrectOperationException
import java.lang.reflect.Field
import pl.mjedynak.idea.plugins.generator.GuavaHashCodeGenerator
import pl.mjedynak.idea.plugins.generator.GuavaEqualsGenerator

class GenerateEqualsHashCodeDeluxeActionHandler extends GenerateEqualsHandler {

    GuavaHashCodeGenerator guavaHashCodeGenerator
    GuavaEqualsGenerator guavaEqualsGenerator

    GenerateEqualsHashCodeDeluxeActionHandler(GuavaHashCodeGenerator guavaHashCodeGenerator, GuavaEqualsGenerator guavaEqualsGenerator) {
        this.guavaHashCodeGenerator = guavaHashCodeGenerator
        this.guavaEqualsGenerator = guavaEqualsGenerator
    }


    @Override
    protected List<? extends GenerationInfo> generateMemberPrototypes(PsiClass psiClass, ClassMember[] originalMembers) throws IncorrectOperationException {

        def hashCodeMethod = guavaHashCodeGenerator.hashCodeMethod(getFieldValue('myHashCodeFields') as List)
        def equalsMethod = guavaEqualsGenerator.equalsMethod(getFieldValue('myEqualsFields') as List, psiClass)

        OverrideImplementUtil.convert2GenerationInfos([hashCodeMethod, equalsMethod])
    }

    def getFieldValue = {  fieldName ->
        def fields = this.class.superclass.getDeclaredFields()
        Field field = fields.find { it.name == fieldName }
        field.setAccessible( true )
        return field.get( this )
    }
}
