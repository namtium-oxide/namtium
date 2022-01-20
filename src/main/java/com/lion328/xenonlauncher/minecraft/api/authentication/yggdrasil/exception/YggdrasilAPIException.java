// Copyright (C) 2016-2022 Waritnan Sookbuntherng
// SPDX-License-Identifier: Apache-2.0

package com.lion328.xenonlauncher.minecraft.api.authentication.yggdrasil.exception;

import com.lion328.xenonlauncher.minecraft.api.authentication.exception.MinecraftAuthenticatorException;

public class YggdrasilAPIException extends MinecraftAuthenticatorException {

    public YggdrasilAPIException() {
        super();
    }

    public YggdrasilAPIException(String message) {
        super(message);
    }

    public YggdrasilAPIException(String message, Throwable cause) {
        super(message, cause);
    }

    public YggdrasilAPIException(Throwable cause) {
        super(cause);
    }

    protected YggdrasilAPIException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
