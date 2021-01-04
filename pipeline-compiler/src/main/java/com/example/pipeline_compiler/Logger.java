package com.example.pipeline_compiler;

import javax.annotation.processing.Messager;

import static javax.tools.Diagnostic.Kind.ERROR;
import static javax.tools.Diagnostic.Kind.NOTE;
import static javax.tools.Diagnostic.Kind.WARNING;

class Logger {

    private Messager mLogger;

    Logger(Messager mLogger) {
        this.mLogger = mLogger;
    }

    void i(String str) {
        if (str != null) {
            mLogger.printMessage(NOTE, str);
        }
    }

    void w(String str) {
        if (str != null) {
            mLogger.printMessage(WARNING, str);
        }
    }

    void e(String str) {
        if (str != null) {
            mLogger.printMessage(ERROR, str);
        }
    }
}
