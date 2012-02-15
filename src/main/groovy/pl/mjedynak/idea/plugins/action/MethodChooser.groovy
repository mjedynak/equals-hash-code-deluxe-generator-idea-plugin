package pl.mjedynak.idea.plugins.action

import com.intellij.psi.PsiClass
import com.intellij.psi.util.PsiUtil

class MethodChooser {

    static final String GUAVA_EQUALS_METHOD = 'equal'
    static final String JAVA_7_EQUALS_METHOD = 'equals'
    static final String GUAVA_HASH_CODE_METHOD = 'hashCode'
    static final String JAVA_7_HASH_CODE_METHOD = 'hash'

    String chooseEqualsMethodName(PsiClass psiClass) {
        PsiUtil.isLanguageLevel7OrHigher(psiClass) ? JAVA_7_EQUALS_METHOD : GUAVA_EQUALS_METHOD
    }

    String chooseHashCodeMethodName(PsiClass psiClass) {
        PsiUtil.isLanguageLevel7OrHigher(psiClass) ? JAVA_7_HASH_CODE_METHOD : GUAVA_HASH_CODE_METHOD
    }
}
