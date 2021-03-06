package com.hadroncfy.jphp.jzend.compile.ins;

/**
 * Created by cfy on 16-5-12.
 */
public interface Opcode {
    int POP = 0,//
    PLUS = 1,//
    MINUS = 2,//
    TIMES = 3,//
    DIVIDE = 4,///
    MOD = 5,//
    BITAND = 6,//
    BITOR = 7,//
    BITXOR = 8,//
    BITNOT = 9,//
    AND = 10,//
    OR = 11,//
    XOR = 12,//
    NOT = 13,//
    LSHIFT = 14,//
    RSHIFT = 15,//
    EQU = 16,//
    NEQU = 17,//
    IDENTICAL = 18,//
    NIDENTICAL = 19,//
    MT = 20,//
    MTOE = 21,//
    LT = 22,//
    LTOE = 23,//
    NUMBER = 24,//
    STRING = 25,//
    PRE_INC = 26,//
    PRE_DEC = 27,//
    POST_INC = 28,//
    POST_DEC = 29,//
    CONCAT = 30,//
    FIND_VARIABLE = 31,//
    TOSTRING = 32,//
    REQUEST_MEMBER = 33,//
    SUBSCRIPT = 34,//
    FIND_VARIABLE_AS_REFERENCE = 35,//
    FUNCTION_CALL = 36,//
    FIND_VARIABLE_BY_NAME = 37,//
    ECHO = 38,//
    PRINT = 39,//
    EXIT = 40,//
    FIND_FUNCTION = 41,//
    FIND_CONST = 42,//
    ASSIGN = 43,//
    SUBSCRIPT_AS_REFERENCE = 44,//
    REQUEST_MEMBER_AS_REFERENCE = 45,//
    FIND_VARIABLE_BY_NAME_AS_REFERENCE = 46,//
    MAX_SUBSCRIPT = 47,//
    MAX_SUBSCRIPT_AS_REFERENCE = 48,//
    DUP = 49,//
    DEREFERENCE = 50,//
    CLONE = 51,//
    PACK_ARG = 52,//
    NEW = 53,//
    FIND_CLASS = 54,//
    GOTO = 55,//
    CONDITIONAL_GOTO = 56,//
    NULL = 57,//
    INCLUDE_OR_EVAL = 58,//
    BREAK = 59,//
    CONTINUE = 60,//
    NEW_ARRAY = 61,//
    ADD_ARRAY_ITEM = 62,//
    CHECK_REF = 63,//
    IS_SET = 64,//
    OPTIONAL_GOTO = 65,//
    ARRAY_ASSIGN = 66,//
    NEGTIVE = 67,//
    ADD_ARRAY_MAP_ITEM = 68,//
    BEGIN_SILENT = 69,//
    END_SILENT = 70,//
    UNSET = 71,//
    DECLARE_CONST = 72,
    FETCH_CLASS_STATIC = 73,//
    GLOBAL = 74,//
    BEGIN_TRY = 75,
    CONDITIONAL_NOT_GOTO = 76,//
    RETURN = 77,//
    THROW = 78,//
    REQUEST_FUNCTION_MEMBER = 79,//
    INSTANCEOF = 80,//
    IS_EMPTY = 81,//
    TLOAD = 82,//
    TSTORE = 83,//
    INTEGER = 84,//

    ARRAY_ITERATOR = 85,//
    MAP_ITERATOR = 86,//
    ITERATOR_END = 87,//
    ITERATOR_NEXT = 88,///
    ARRAY_ITERATOR_GET = 89,//
    MAP_ITERATOR_KEY = 90,//
    MAP_ITERATOR_VALUE = 91,//

    FIND_CLASS_VAR = 92,//
    FIND_CLASS_VAR_BY_NAME= 93,//
    FIND_CLASS_CONST = 94,//
    FIND_CLASS_FUNCTION = 95,//
    FETCH_CLASS_SELF = 96,//
    FETCH_CLASS_PARENT = 97,//

    INT_CAST = 98,//
    FLOAT_CAST = 99,//
    STRING_CAST = 100,//
    ARRAY_CAST = 101,//
    OBJECT_CAST = 102,//
    BOOL_CAST = 103,//
    UNSET_CAST = 104,//
    FIND_CONST_BY_NAME = 105,//

    NEW_FUNCTION = 106;

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
            "INCLUDE_OR_EVAL",
            "BREAK",
            "CONTINUE",
            "NEW_ARRAY",
            "ADD_ARRAY_ITEM",
            "CHECK_REF",
            "IS_SET",
            "OPTIONAL_GOTO",
            "ARRAY_ASSIGN",
            "NEGTIVE",
            "ADD_ARRAY_MAP_ITEM",
            "BEGIN_SILENT",
            "END_SILENT",
            "UNSET",
            "DECLARE_CONST",
            "FETCH_CLASS_STATIC",
            "GLOBAL",
            "BEGIN_TRY",
            "CONDITIONAL_NOT_GOTO",
            "RETURN",
            "THROW",
            "REQUEST_FUNCTION_MEMBER",
            "INSTANCEOF",
            "IS_EMPTY",
            "TLOAD",
            "TSTORE",
            "INTEGER",
            "ARRAY_ITERATOR",
            "MAP_ITERATOR",
            "ITERATOR_END",
            "ITERATOR_NEXT",
            "ARRAY_ITERATOR_GET",
            "MAP_ITERATOR_KEY",
            "MAP_ITERATOR_VALUE",
            "FIND_CLASS_VAR",
            "FIND_CLASS_VAR_BY_NAME",
            "FIND_CLASS_CONST",
            "FIND_CLASS_FUNCTION",
            "FETCH_CLASS_SELF",
            "FETCH_CLASS_PARENT",
            "INT_CAST",
            "FLOAT_CAST",
            "STRING_CAST",
            "ARRAY_CAST",
            "OBJECT_CAST",
            "BOOL_CAST",
            "UNSET_CAST",
            "FIND_CONST_BY_NAME",
            "NEW_FUNCTION"
    };
}
