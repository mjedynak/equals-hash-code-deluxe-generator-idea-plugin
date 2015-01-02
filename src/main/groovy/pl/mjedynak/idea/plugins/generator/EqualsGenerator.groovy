package pl.mjedynak.idea.plugins.generator

import com.intellij.pom.java.LanguageLevel
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElementFactory
import com.intellij.psi.PsiField
import com.intellij.psi.PsiMethod
import groovy.transform.TypeChecked
import org.jetbrains.annotations.NotNull
import pl.mjedynak.idea.plugins.model.EqualsAndHashCodeType

@TypeChecked
class EqualsGenerator {

    private EqualsMethodTextCreator equalsMethodTextCreator

    EqualsGenerator(EqualsMethodTextCreator equalsMethodTextCreator) {
        this.equalsMethodTextCreator = equalsMethodTextCreator
    }

    PsiMethod equalsMethod(@NotNull List<PsiField> equalsPsiFields, PsiClass psiClass, EqualsAndHashCodeType equalsAndHashCodeType) {
        if (!equalsPsiFields.isEmpty()) {
            PsiElementFactory factory = getFactory(equalsPsiFields[0])
            String methodText = equalsMethodTextCreator.createMethodText(equalsPsiFields, psiClass, equalsAndHashCodeType)
            factory.createMethodFromText(methodText, null, LanguageLevel.JDK_1_6)
        }
    }

    private PsiElementFactory getFactory(PsiField psiField) {
        JavaPsiFacade.getInstance(psiField.project).elementFactory
    }
}
