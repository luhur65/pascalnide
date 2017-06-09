/*
 *  Copyright (c) 2017 Tran Le Duy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.duy.pascal.frontend.debug;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.duy.pascal.backend.ast.VariableDeclaration;
import com.duy.pascal.backend.ast.codeunit.library.RuntimeUnitPascal;
import com.duy.pascal.backend.ast.codeunit.program.RuntimePascalProgram;
import com.duy.pascal.backend.ast.instructions.with_statement.WithOnStack;
import com.duy.pascal.backend.ast.runtime_value.FunctionOnStack;
import com.duy.pascal.backend.ast.runtime_value.VariableContext;
import com.duy.pascal.backend.data_types.RuntimeType;
import com.duy.pascal.backend.runtime_exception.RuntimePascalException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Duy on 08-Jun-17.
 */

public class CallStack {
    @Nullable
    private VariableContext parentContext;
    @NonNull
    private VariableContext currentContext;

    public CallStack(@NonNull VariableContext currentContext) {
        this.currentContext = currentContext;
    }

    public ArrayList getAllVariables() {
        return null;
    }

    public List<String> getDefineVariableNames() {
        return currentContext.getUserDefineVariableNames();
    }

    @SuppressWarnings("unchecked")
    public List<VariableDeclaration> getDefineVars() {
        ArrayList<VariableDeclaration> result = new ArrayList<>();
        if (currentContext instanceof FunctionOnStack) {
            FunctionOnStack f = (FunctionOnStack) currentContext;

            //clone add variable
            ArrayList<VariableDeclaration> variables = f.getPrototype().getDeclaration().getVariables();
            for (VariableDeclaration variable : variables) result.add(variable.clone());

            String[] argumentNames = f.getPrototype().getArgumentNames();
            RuntimeType[] argumentTypes = f.getPrototype().getArgumentTypes();
            for (int i = 0; i < argumentNames.length; i++) {
                try {
                    result.add(new VariableDeclaration(argumentNames[i], argumentTypes[i].getDeclType(),
                            f.getVar(argumentNames[i]), null));
                } catch (RuntimePascalException e) {
                    e.printStackTrace();
                }
            }
        } else if (currentContext instanceof RuntimePascalProgram) {
            RuntimePascalProgram program = (RuntimePascalProgram) currentContext;
            ArrayList<VariableDeclaration> variables = program.getDeclaration().getContext().getVariables();
            for (VariableDeclaration variable : variables) result.add(variable.clone());
        } else if (currentContext instanceof RuntimeUnitPascal) {
            RuntimeUnitPascal unit = (RuntimeUnitPascal) currentContext;
            ArrayList<VariableDeclaration> variables = unit.getDeclaration().getContext().getVariables();
            for (VariableDeclaration variable : variables) result.add(variable.clone());
        } else if (currentContext instanceof WithOnStack) {
            WithOnStack withOnStack = (WithOnStack) currentContext;
            ArrayList<VariableDeclaration> variables = withOnStack.getDeclaration().getVariableDeclarations();
            for (VariableDeclaration variable : variables) result.add(variable.clone());
        }
        return result;
    }

    public HashMap<String, ?> getMapVars() {
        return currentContext.getMapVars();
    }

    public Object getValue(String name) {
        try {
            return currentContext.getLocalVar(name);
        } catch (RuntimePascalException e) {
            return null;
        }
    }

    public VariableContext getRootDepth(int depth) {
        return null;
    }

    public ArrayList<VariableContext> getStacks() {
        ArrayList<VariableContext> stacks = new ArrayList<>();
        stacks.add(currentContext);
        VariableContext tmp = currentContext.getParentContext();
        while (tmp != null) {
            stacks.add(0, tmp);
            tmp = tmp.getParentContext();
        }
        return stacks;
    }

}
