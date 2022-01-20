// Copyright (C) 2016-2022 Waritnan Sookbuntherng
// SPDX-License-Identifier: Apache-2.0

package com.lion328.xenonlauncher.minecraft.api.authentication.exception;

public class MinecraftAuthenticatorException extends Exception
{

    public MinecraftAuthenticatorException()
    {
        super();
    }

    public MinecraftAuthenticatorException(String message)
    {
        super(message);
    }

    public MinecraftAuthenticatorException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public MinecraftAuthenticatorException(Throwable cause)
    {
        super(cause);
    }

    protected MinecraftAuthenticatorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
    {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
