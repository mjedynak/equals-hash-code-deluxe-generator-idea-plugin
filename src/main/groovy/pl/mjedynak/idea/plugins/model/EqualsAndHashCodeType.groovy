package pl.mjedynak.idea.plugins.model

import groovy.transform.CompileStatic

@CompileStatic
enum EqualsAndHashCodeType {
    JAVA_7(JAVA_7_EQUALS_METHOD, JAVA_7_HASH_CODE_METHOD, JAVA_7_ARRAY_COMPARISON_METHOD),
    GUAVA(GUAVA_EQUALS_METHOD, GUAVA_HASH_CODE_METHOD, GUAVA_ARRAY_COMPARISON_METHOD)

    static final String JAVA_7_EQUALS_METHOD = 'equals'
    static final String GUAVA_EQUALS_METHOD = 'equal'
    static final String JAVA_7_HASH_CODE_METHOD = 'hash'
    static final String GUAVA_HASH_CODE_METHOD = 'hashCode'
    static final String JAVA_7_ARRAY_COMPARISON_METHOD = 'Objects.deepEquals'
    static final String GUAVA_ARRAY_COMPARISON_METHOD = 'Arrays.equals'

    private final String equalsMethodName
    private final String hashCodeMethodName
    private final String arrayComparisonMethodName

    EqualsAndHashCodeType(String equalsMethodName, String hashCodeMethodName, String arrayComparisonMethodName) {
        this.equalsMethodName = equalsMethodName
        this.hashCodeMethodName = hashCodeMethodName
        this.arrayComparisonMethodName = arrayComparisonMethodName
    }

    @SuppressWarnings('ConfusingMethodName')
    String equalsMethodName() {
        equalsMethodName
    }

    @SuppressWarnings('ConfusingMethodName')
    String hashCodeMethodName() {
        hashCodeMethodName
    }

    @SuppressWarnings('ConfusingMethodName')
    String arrayComparisonMethodName() {
        arrayComparisonMethodName
    }
}
