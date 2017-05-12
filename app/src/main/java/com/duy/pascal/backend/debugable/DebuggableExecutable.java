package com.duy.pascal.backend.debugable;

import com.js.interpreter.ast.instructions.Executable;
import com.js.interpreter.ast.instructions.ExecutionResult;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;
import com.js.interpreter.runtime.exception.UnhandledPascalException;

/**
 * DEBUG class
 * this class can check stack size
 */
public abstract class DebuggableExecutable implements Executable {

    @Override
    public ExecutionResult execute(VariableContext f, RuntimeExecutable<?> main)
            throws RuntimePascalException {
        try {
            if (main != null) {
                if (main.isDebugMode()) {
                    main.getDebugListener().onLine(getLineNumber());
                }
                main.incStack(getLineNumber());
                main.scriptControlCheck(getLineNumber());
            }
            ExecutionResult result = executeImpl(f, main);
            if (main != null) {
                main.decStack();
            }
            return result;
        } catch (RuntimePascalException e) {
            throw e;
        } catch (Exception e) {
            throw new UnhandledPascalException(this.getLineNumber(), e);
        }
    }

    public abstract ExecutionResult executeImpl(VariableContext context, RuntimeExecutable<?> main)
            throws RuntimePascalException;
}
