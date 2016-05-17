package com.hadroncfy.jphp.jzend.compile;

/**
 * Created by cfy on 16-5-12.
 */
public interface Opcode {
    int POP = 0,
    PLUS = 1,
    MINUS = 2,
    TIMES = 3,
    DIVIDE = 4,
    MOD = 5,
    BITAND = 6,
    BITOR = 7,
    BITXOR = 8,
    BITNOT = 9,
    AND = 10,
    OR = 11,
    XOR = 12,
    NOT = 13,
    LSHIFT = 14,
    RSHIFT = 15,
    EQU = 16,
    NEQU = 17,
    IDENTICAL = 18,
    NIDENTICAL = 19,
    MT = 20,
    MTOE = 21,
    LT = 22,
    LTOE = 23,
    NUMBER = 24,
    STRING = 25,
    PRE_INC = 26,
    PRE_DEC = 27,
    POST_INC = 28,
    POST_DEC = 29,
    CONCAT = 30,
    FIND_VARIABLE = 31,
    TOSTRING = 32,
    REQUEST_MEMBER = 33,
    SUBSCRIPT = 34,
    FIND_VARIABLE_AS_REFERENCE = 35,
    FUNCTION_CALL = 36,
    FIND_VARIABLE_BY_NAME = 37,
    ECHO = 38,
    PRINT = 39,
    EXIT = 40,
    FIND_FUNCTION = 41,
    FIND_CONST = 42,
    ASSIGN = 43,
    SUBSCRIPT_AS_REFERENCE = 44,
    REQUEST_MEMBER_AS_REFERENCE = 45,
    FIND_VARIABLE_BY_NAME_AS_REFERENCE = 46,
    MAX_SUBSCRIPT = 47,
    MAX_SUBSCRIPT_AS_REFERENCE = 48,
    DUP = 49,
    DEREFERENCE = 50,
    CLONE = 51,
    PACK_ARG = 52,
    NEW = 53,
    FIND_CLASS = 54,
    GOTO = 55,
    CONDITIONAL_GOTO = 56,
    NULL = 57,
    BEGIN_LOOP = 58,
    BREAK = 59,
    CONTINUE = 60,
    NEW_ARRAY = 61,
    ADD_ARRAY_ITEM = 62;

    String[] ins_names = {
            "POP",
            "PLUS",
            "MINUS",
            "TIMES",
            "DIVIDE",
            "MOD",
            "BITAND",
            "BITOR",
            "BITXOR",
            "BITNOT",
            "AND",
            "OR",
            "XOR",
            "NOT",
            "LSHIFT",
            "RSHIFT",
            "EQU",
            "NEQU",
            "IDENTICAL",
            "NIDENTICAL",
            "MT",
            "MTOE",
            "LT",
            "LTOE",
            "NUMBER",
            "STRING",
            "PRE_INC",
            "PRE_DEC",
            "POST_INC",
            "POST_DEC",
            "CONCAT",
            "FIND_VARIABLE",
            "TOSTRING",
            "REQUEST_MEMBER",
            "SUBSCRIPT",
            "FIND_VARIABLE_AS_REFERENCE",
            "FUNCTION_CALL",
            "FIND_VARIABLE_BY_NAME",
            "ECHO",
            "PRINT",
            "EXIT",
            "FIND_FUNCTION",
            "FIND_CONST",
            "ASSIGN",
            "SUBSCRIPT_AS_REFERENCE",
            "REQUEST_MEMBER_AS_REFERENCE",
            "FIND_VARIABLE_BY_NAME_AS_REFERENCE",
            "MAX_SUBSCRIPT",
            "MAX_SUBSCRIPT_AS_REFERENCE",
            "DUP",
            "DEREFERENCE",
            "CLONE",
            "PACK_ARG",
            "NEW",
            "FIND_CLASS",
            "GOTO",
            "CONDITIONAL_GOTO",
            "NULL",
            "BEGIN_LOOP",
            "BREAK",
            "CONTINUE",
            "NEW_ARRAY",
            "ADD_ARRAY_ITEM"
    };
}
