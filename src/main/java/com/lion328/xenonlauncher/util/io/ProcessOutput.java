// Copyright (C) 2017-2022 Waritnan Sookbuntherng
// SPDX-License-Identifier: Apache-2.0

package com.lion328.xenonlauncher.util.io;

import java.io.InputStream;

public class ProcessOutput {

    private final Process process;
    private final InputStream out;
    private final InputStream err;

    public ProcessOutput(Process process) {
        this(process, process.getInputStream(), process.getErrorStream());
    }

    public ProcessOutput(Process process, InputStream out, InputStream err) {
        this.process = process;
        this.out = out;
        this.err = err;
    }

    public boolean isRunning() {
        try {
            process.exitValue();
        } catch (IllegalThreadStateException ignore) {
            return false;
        }

        return true;
    }

    public InputStream getInputStream() {
        return out;
    }

    public InputStream getErrorStream() {
        return err;
    }
}
