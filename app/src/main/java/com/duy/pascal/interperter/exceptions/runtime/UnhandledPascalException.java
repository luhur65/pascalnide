package com.duy.pascal.interperter.exceptions.runtime;

import com.duy.pascal.interperter.linenumber.LineInfo;

public class UnhandledPascalException extends RuntimePascalException {
    private Exception cause;

    public UnhandledPascalException(LineInfo line, Exception cause) {
        super(line);
        this.cause = cause;
    }

    public UnhandledPascalException(LineInfo lineNumber, Throwable e) {
        super(lineNumber);
        this.cause = new RuntimeException(e);
    }

    @Override
    public String getMessage() {
        return line + " Runtime pascal exception.";
    }

    @Override
    public Exception getCause() {
        return cause;
    }
}
