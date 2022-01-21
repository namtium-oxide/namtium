// Copyright (C) 2019-2022 Waritnan Sookbuntherng
// SPDX-License-Identifier: Apache-2.0

package com.lion328.namtium.launcher.exception;

public class TooLowMaximumMemoryException extends Exception {

    private final int memory;
    private final int minimum;

    public TooLowMaximumMemoryException(int memory, int minimum) {
        this.memory = memory;
        this.minimum = minimum;
    }

    public int getMemoryInMB() {
        return memory;
    }

    public int getMinimumMemoryInMB() {
        return minimum;
    }
}
