package com.hadroncfy.jphp.jzend.compile;

/**
 * Created by cfy on 16-8-30.
 *
 */
public interface WarningReporter {
    void addWarning(String msg);
    void addNotice(String msg);
}
