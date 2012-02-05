package pl.mjedynak.idea.plugins.generator

import com.intellij.pom.java.LanguageLevel
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElementFactory
import com.intellij.psi.PsiField
import com.intellij.psi.PsiMethod
import org.jetbrains.annotations.NotNull

class GuavaEqualsGenerator {

    PsiMethod equalsMethod(@NotNull List<PsiField> equalsPsiFields, PsiClass psiClass) {
        if (!equalsPsiFields.isEmpty()) {
            PsiElementFactory factory = getFactory(equalsPsiFields[0])
//            def fieldsString = equalsPsiFields.collect {it.name}.join(",")
            StringBuilder methodText = new StringBuilder()
            methodText << "@Override public boolean equals(Object obj) {"
            methodText << " if (obj == null) {return false;}"
            methodText << " if (getClass() != obj.getClass()) {return false;}"
            methodText << " final ${psiClass.text} other = (${psiClass.text}) obj;"
            methodText << " return Objects.equals(${equalsPsiFields[0].name});}"
            factory.createMethodFromText(methodText.toString(), null, LanguageLevel.JDK_1_6)
        }
    }

    private PsiElementFactory getFactory(PsiField psiField) {
        return JavaPsiFacade.getInstance(psiField.project).getElementFactory()
    }
}


