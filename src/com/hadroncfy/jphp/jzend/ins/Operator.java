package com.hadroncfy.jphp.jzend.ins;

/**
 * Created by cfy on 16-8-31.
 */
public class Operator {
    public static final int BINARY_PLUS = 0;
    public static final int BINARY_MINUS = 1;
    public static final int BINARY_TIMES = 2;
    public static final int BINARY_DIVIDE = 3;
    public static final int BINARY_MOD = 4;
    public static final int BINARY_LESS_THAN = 5;
    public static final int BINARY_MORE_THAN = 6;
    public static final int BINARY_EQUAL = 7;
    public static final int BINARY_IDENTICAL = 8;
    public static final int BINARY_AND = 9;
    public static final int BINARY_OR = 10;
    public static final int BINARY_BIT_AND = 11;
    public static final int BINARY_BIT_OR = 12;
    public static final int BINARY_BIT_XOR = 13;
    public static final int BINARY_ASSIGN = 14;
    public static final int BINARY_INSTANCEOF = 15;

    public static final int UNARY_NOT = 0;
    public static final int UNARY_BIT_NOT = 1;
    public static final int UNARY_POS = 2;
    public static final int UNARY_NEG = 3;
    public static final int UNARY_INT_CAST = 4;
    public static final int UNARY_FLOAT_CAST = 5;
    public static final int UNARY_STRING_CAST = 6;
    public static final int UNARY_BOOL_CAST = 7;
    public static final int UNARY_ARRAY_CAST = 8;
    public static final int UNARY_POST_INC = 9;
    public static final int UNARY_POST_DEC = 10;
    public static final int UNARY_PRE_INC = 11;
    public static final int UNARY_PRE_DEC = 12;
    public static final int UNARY_OBJECT_CAST = 13;

    private static final String[] binaryS = {"+","-","*","/","%","<",">","==","===","&&","||","&","|","^","=","instanceof"};
    private static final String[] unaryS = {"!","~","+","-","(int)","(float)","(string)","(bool)","(array)","()++","++()","()--","--()","(object)"};

    public static String toBinaryOptrString(int i){
        return binaryS[i];
    }
    public static String toUnaryOptrString(int i){
        return unaryS[i];
    }
}
