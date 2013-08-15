package pl.mjedynak.idea.plugins.action

import com.intellij.openapi.util.Computable
import com.intellij.psi.PsiMethod
import com.intellij.util.IncorrectOperationException
import groovy.transform.CompileStatic

@CompileStatic
class DeleteExistingMethodsComputable implements Computable<Boolean> {
    PsiMethod equalsMethod
    PsiMethod hashCodeMethod

    DeleteExistingMethodsComputable(PsiMethod equalsMethod, PsiMethod hashCodeMethod) {
        this.equalsMethod = equalsMethod
        this.hashCodeMethod = hashCodeMethod
    }

    Boolean compute() {
        try {
            equalsMethod?.delete()
            hashCodeMethod?.delete()
            true
        }
        catch (IncorrectOperationException e) {
            false
        }
    }
}
