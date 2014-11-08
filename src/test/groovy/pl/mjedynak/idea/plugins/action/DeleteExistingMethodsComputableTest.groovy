package pl.mjedynak.idea.plugins.action

import com.intellij.psi.impl.source.PsiMethodImpl
import com.intellij.util.IncorrectOperationException
import spock.lang.Specification

class DeleteExistingMethodsComputableTest extends Specification {

    PsiMethodImpl equalsMethod = Mock()
    PsiMethodImpl hashCodeMethod = Mock()

    DeleteExistingMethodsComputable computable = new DeleteExistingMethodsComputable(equalsMethod, hashCodeMethod)

    def "deletes both methods and returns true"() {
        when:
        boolean result = computable.compute()

        then:
        result == true
        equalsMethod.delete()
        hashCodeMethod.delete()
    }

    def "deletes method only if it is not null"() {
        hashCodeMethod = null
        computable = new DeleteExistingMethodsComputable(equalsMethod, hashCodeMethod)
        when:
        boolean result = computable.compute()

        then:
        result == true
        equalsMethod.delete()
        0 * hashCodeMethod._
    }

    def "returns false when IncorrectOperationException is thrown"() {
        equalsMethod.delete() >> { throw new IncorrectOperationException() }

        when:
        boolean result = computable.compute()

        then:
        result == false
    }

}
