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

package com.lion328.xenonlauncher.minecraft.api.authentication.yggdrasil.exception;

import com.google.gson.annotations.SerializedName;

public class YggdrasilErrorMessage
{

    @SerializedName("error")
    private String shortMessage;
    @SerializedName("errorMessage")
    private String longMessage;
    @SerializedName("cause")
    private String cause;

    public YggdrasilErrorMessage()
    {

    }

    public YggdrasilErrorMessage(String shortMessage, String longMessage, String cause)
    {
        this.shortMessage = shortMessage;
        this.longMessage = longMessage;
        this.cause = cause;
    }

    public YggdrasilAPIException toException()
    {
        String message = shortMessage + ": " + longMessage;

        if (cause != null)
        {
            message += " (" + cause + ")";
        }

        return new YggdrasilAPIException(message);
    }

    public String getShortMessage()
    {
        return shortMessage;
    }

    public String getLongMessage()
    {
        return longMessage;
    }

    public String getCause()
    {
        return cause;
    }
}
