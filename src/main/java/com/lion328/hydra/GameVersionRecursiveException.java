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
