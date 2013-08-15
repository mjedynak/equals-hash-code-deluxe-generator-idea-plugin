package pl.mjedynak.idea.plugins.psi

import com.intellij.psi.CommonClassNames
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiModifier
import groovy.transform.CompileStatic

@CompileStatic
class EqualsFinder {

    boolean hasEqualsMethod(PsiClass psiClass) {
        psiClass.findMethodsByName("equals", false).any { PsiMethod method ->
            isPublic(method) && isNotStatic(method) && hasObjectAsParameter(method) && returnsBoolean(method)
        }
    }

    private static boolean isNotStatic(PsiMethod method) {
        !method.hasModifierProperty(PsiModifier.STATIC)
    }

    private static boolean returnsBoolean(PsiMethod method) {
        method.returnType.equalsToText("boolean")
    }

    private static boolean isPublic(PsiMethod method) {
        method.hasModifierProperty(PsiModifier.PUBLIC)
    }

    private static boolean hasObjectAsParameter(PsiMethod method) {
        method.getParameterList()?.parameters?.size() == 1 && method.getParameterList().parameters[0].type.canonicalText == CommonClassNames.JAVA_LANG_OBJECT
    }
}
