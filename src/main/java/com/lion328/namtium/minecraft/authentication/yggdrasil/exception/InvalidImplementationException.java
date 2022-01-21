// Copyright (C) 2016-2022 Waritnan Sookbuntherng
// SPDX-License-Identifier: Apache-2.0

package com.lion328.namtium.minecraft.authentication.yggdrasil.exception;

public class InvalidImplementationException extends YggdrasilAPIException {

    public InvalidImplementationException() {
        super();
    }

    public InvalidImplementationException(String message) {
        super(message);
    }

    public InvalidImplementationException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidImplementationException(Throwable cause) {
        super(cause);
    }

    protected InvalidImplementationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
