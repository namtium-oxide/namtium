// Copyright (C) 2016-2022 Waritnan Sookbuntherng
// SPDX-License-Identifier: Apache-2.0

package com.lion328.hydra;

import java.io.IOException;

public class GameVersionRecursiveException extends IOException
{

    public GameVersionRecursiveException()
    {
        super();
    }

    public GameVersionRecursiveException(String message)
    {
        super(message);
    }

    public GameVersionRecursiveException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public GameVersionRecursiveException(Throwable cause)
    {
        super(cause);
    }
}
