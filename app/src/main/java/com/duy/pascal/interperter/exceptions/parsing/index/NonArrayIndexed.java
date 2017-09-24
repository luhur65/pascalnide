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

package com.duy.pascal.interperter.exceptions.parsing.index;

import android.support.annotation.NonNull;

import com.duy.pascal.interperter.declaration.lang.types.Type;
import com.duy.pascal.interperter.exceptions.parsing.ParsingException;
import com.duy.pascal.interperter.linenumber.LineInfo;

import org.jetbrains.annotations.Nullable;

public class NonArrayIndexed extends ParsingException {
    @NonNull
    private Type type;

    public NonArrayIndexed(@NonNull LineInfo line, @NonNull Type type) {
        super(line);
        this.type = type;
    }

    @Nullable
    public String getMessage() {
        return "Tried to do indexed access on something which wasn\'t an array or a string. It was a " + this.type.toString();
    }

    @NonNull
    public final Type getType() {
        return this.type;
    }

    public final void setType(@NonNull Type var1) {
        this.type = var1;
    }
}
