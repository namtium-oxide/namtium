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

package com.lion328.xenonlauncher.minecraft.launcher.json.data;

import com.google.gson.annotations.SerializedName;
import com.lion328.xenonlauncher.util.OS;

public class LibraryRule
{

    @SerializedName("action")
    private LibraryRuleAction action;
    @SerializedName("os")
    private SystemIdentifier identifier;

    public LibraryRule()
    {
        this.action = LibraryRuleAction.ALLOW;
    }

    public LibraryRule(LibraryRuleAction action, SystemIdentifier identifier)
    {
        this.action = action;
        this.identifier = identifier;
    }

    public LibraryRuleAction getAction()
    {
        return action;
    }

    public SystemIdentifier getIdentifier()
    {
        return identifier;
    }

    @Deprecated
    public boolean isAllowed(OS os, String version)
    {
        return (action == LibraryRuleAction.ALLOW) == (identifier == null || identifier.isMatch(os, version));
    }

    public static class SystemIdentifier
    {

        @SerializedName("name")
        private OS os;
        @SerializedName("version")
        private String versionPattern;

        public SystemIdentifier()
        {

        }

        public SystemIdentifier(OS os)
        {
            this(os, "(.*?)");
        }

        public SystemIdentifier(OS os, String versionPattern)
        {
            this.os = os;
            this.versionPattern = versionPattern;
        }

        public OS getOS()
        {
            return os;
        }

        public boolean isMatch(OS os, String version)
        {
            return this.os == os && (versionPattern == null || version.matches(versionPattern));
        }
    }
}
