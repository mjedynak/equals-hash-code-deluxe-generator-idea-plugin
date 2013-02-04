package pl.mjedynak.idea.plugins.model

import spock.lang.Specification

import static pl.mjedynak.idea.plugins.model.EqualsAndHashCodeType.*

class EqualsAndHashCodeTypeTest extends Specification {

    def "has specific equals method name for java 7"() {
        when:
        String name = JAVA_7.equalsMethodName()

        then:
        name == JAVA_7_EQUALS_METHOD
    }

    def "has specific equals method name for guava"() {
        when:
        String name = GUAVA.equalsMethodName()

        then:
        name == GUAVA_EQUALS_METHOD
    }

    def "has specific hashcode method name for java 7"() {
        when:
        String name = JAVA_7.hashCodeMethodName()

        then:
        name == JAVA_7_HASH_CODE_METHOD
    }

    def "has specific hashcode method name for guava"() {
        when:
        String name = GUAVA.hashCodeMethodName()

        then:
        name == GUAVA_HASH_CODE_METHOD
    }
}
