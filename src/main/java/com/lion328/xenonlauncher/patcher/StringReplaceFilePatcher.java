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

package com.lion328.xenonlauncher.patcher;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.List;

public class StringReplaceFilePatcher implements FilePatcher
{

    private final String find;
    private final String replace;

    public StringReplaceFilePatcher(String find, String replace)
    {
        this.find = find;
        this.replace = replace;
    }

    @Override
    public byte[] patchFile(String name, byte[] original)
    {
        if (!name.endsWith(".class"))
        {
            return original;
        }

        ClassReader reader = new ClassReader(original);
        ClassNode node = new ClassNode();
        reader.accept(node, 0);

        List fields = node.fields;
        FieldNode field;
        for (Object obj : fields)
        {
            if (!(obj instanceof FieldNode))
            {
                continue;
            }

            field = (FieldNode) obj;

            if (field.value instanceof String)
            {
                field.value = ((String) field.value).replace(find, replace);
            }
        }

        List methods = node.methods;
        MethodNode method;
        for (Object obj : methods)
        {
            if (!(obj instanceof MethodNode))
            {
                continue;
            }

            method = (MethodNode) obj;

            InsnList insns = method.instructions;

            for (int i = 0; i < insns.size(); i++)
            {
                AbstractInsnNode insn = insns.get(i);

                if (!(insn instanceof LdcInsnNode))
                {
                    continue;
                }

                LdcInsnNode ldc = (LdcInsnNode) insn;

                if (!(ldc.cst instanceof String))
                {
                    continue;
                }

                ldc.cst = ((String) ldc.cst).replace(find, replace);
            }
        }

        ClassWriter writer = new ClassWriter(0);
        node.accept(writer);

        return writer.toByteArray();
    }
}
