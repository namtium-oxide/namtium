/*
 * Copyright (c) 2018 Waritnan Sookbuntherng
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.lion328.xenonlauncher.downloader.verifier;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MultipleFileVerifier implements FileVerifier
{

    public static final int LOGIC_OR = 1;
    public static final int LOGIC_AND = 2;

    private final List<FileVerifier> verifiers;
    private final int logicType;

    public MultipleFileVerifier(FileVerifier first, FileVerifier second)
    {
        this(first, second, LOGIC_AND);
    }

    public MultipleFileVerifier(FileVerifier first, FileVerifier second, int logicType)
    {
        this(new ArrayList<FileVerifier>(), logicType);

        verifiers.add(first);
        verifiers.add(second);
    }

    public MultipleFileVerifier(List<FileVerifier> verifiers, int logicType)
    {
        if (logicType != LOGIC_OR && logicType != LOGIC_AND)
        {
            throw new IllegalArgumentException("Logic must be 'or' or 'and' only");
        }

        this.verifiers = verifiers;
        this.logicType = logicType;
    }

    @Override
    public boolean isValid(File file) throws IOException
    {
        for (FileVerifier verifier : verifiers)
        {
            boolean valid = verifier.isValid(file);

            if (logicType == LOGIC_OR && valid)
            {
                return true;
            }
            else if (logicType == LOGIC_AND && !valid)
            {
                return false;
            }
        }

        if (logicType == LOGIC_OR) {
            return false;
        }

        return true;
    }
}
