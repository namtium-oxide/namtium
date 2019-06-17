package com.lion328.hydra;

public class TooLowMaximumMemoryException extends Exception
{

    private final int memory;
    private final int minimum;

    public TooLowMaximumMemoryException(int memory, int minimum)
    {
        this.memory = memory;
        this.minimum = minimum;
    }

    public int getMemoryInMB()
    {
        return memory;
    }

    public int getMinimumMemoryInMB()
    {
        return minimum;
    }
}
