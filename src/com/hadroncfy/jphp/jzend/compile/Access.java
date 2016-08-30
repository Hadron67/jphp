package com.hadroncfy.jphp.jzend.compile;

/**
 * Created by cfy on 16-8-26.
 */
public enum Access {
    PUBLIC,PROTECTED,PRIVATE;

    private static String[] accesses = {"public","protected","private"};

    @Override
    public String toString() {
        return accesses[ordinal()];
    }
}
