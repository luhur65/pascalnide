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

package com.duy.pascal.interperter.exceptions.parsing.define;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.interperter.declaration.Name;
import com.duy.pascal.interperter.declaration.lang.types.Type;
import com.duy.pascal.interperter.exceptions.parsing.ParsingException;


public class VariableIdentifierExpectException extends ParsingException {
    @NonNull
    private Name name;
    @NonNull
    private ExpressionContext scope;
    private Type expectedType;

    public VariableIdentifierExpectException(UnknownIdentifierException e) {
        super(e.getLineInfo());
        this.name = e.getName();
        this.scope = e.getScope();
    }

    @Nullable
    public String getMessage() {
        return "Variable identifier expected: name";
    }

    @NonNull
    public Name getName() {
        return this.name;
    }

    @NonNull
    public ExpressionContext getScope() {
        return this.scope;
    }

    @Override
    public boolean canQuickFix() {
        return true;
    }

    public Type getExpectedType() {
        return expectedType;
    }

    public void setExpectedType(Type declType) {
        this.expectedType = declType;
    }
}