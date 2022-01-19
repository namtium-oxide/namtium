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

package com.lion328.xenonlauncher.proxy.socks5;

public enum AuthenticationMethod
{

    NO_AUTHENTICATION_REQUIRED(0x00),
    GSSAPI(0x01),
    USERNAME_PASSWORD(0x02),
    NO_ACCEPTABLE_METHODS(0xFF),
    UNKNOWN(-1);

    private int b;

    AuthenticationMethod(int b)
    {
        this.b = b;
    }

    public static AuthenticationMethod getByByte(int b)
    {
        for (AuthenticationMethod method : values())
        {
            if (method.b == (b & 0xFF))
            {
                return method;
            }
        }
        return UNKNOWN;
    }

    public int getByte()
    {
        return b & 0xFF;
    }
}
