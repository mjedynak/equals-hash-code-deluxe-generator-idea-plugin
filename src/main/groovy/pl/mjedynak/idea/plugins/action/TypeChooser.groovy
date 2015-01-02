package pl.mjedynak.idea.plugins.action

import com.intellij.psi.PsiClass
import com.intellij.psi.util.PsiUtil
import groovy.transform.TypeChecked
import pl.mjedynak.idea.plugins.model.EqualsAndHashCodeType

import static pl.mjedynak.idea.plugins.model.EqualsAndHashCodeType.GUAVA
import static pl.mjedynak.idea.plugins.model.EqualsAndHashCodeType.JAVA_7

@TypeChecked
class TypeChooser {

    EqualsAndHashCodeType chooseType(PsiClass psiClass) {
        PsiUtil.isLanguageLevel7OrHigher(psiClass) ? JAVA_7 : GUAVA
    }
}
