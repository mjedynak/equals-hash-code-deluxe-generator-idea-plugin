package pl.mjedynak.idea.plugins.model

import groovy.transform.CompileStatic

@CompileStatic
enum EqualsAndHashCodeType {
    JAVA_7(JAVA_7_EQUALS_METHOD, JAVA_7_HASH_CODE_METHOD),
    GUAVA(GUAVA_EQUALS_METHOD, GUAVA_HASH_CODE_METHOD)

    static final String GUAVA_EQUALS_METHOD = 'equal'
    static final String JAVA_7_EQUALS_METHOD = 'equals'
    static final String GUAVA_HASH_CODE_METHOD = 'hashCode'
    static final String JAVA_7_HASH_CODE_METHOD = 'hash'

    private final String equalsMethodName
    private final String hashCodeMethodName

    EqualsAndHashCodeType(String equalsMethodName, String hashCodeMethodName) {
        this.equalsMethodName = equalsMethodName
        this.hashCodeMethodName = hashCodeMethodName
    }

    @SuppressWarnings('ConfusingMethodName')
    String equalsMethodName() {
        equalsMethodName
    }

    @SuppressWarnings('ConfusingMethodName')
    String hashCodeMethodName() {
        hashCodeMethodName
    }
}
