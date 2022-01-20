// Copyright (C) 2017-2022 Waritnan Sookbuntherng
// SPDX-License-Identifier: Apache-2.0

package com.lion328.xenonlauncher.util.io;

import java.io.IOException;
import java.io.OutputStream;

public final class TeeOutputStream extends OutputStream
{

    private final OutputStream out;
    private final OutputStream tee;

    public TeeOutputStream(OutputStream out, OutputStream tee)
    {
        if (out == null || tee == null)
        {
            throw new NullPointerException();
        }

        this.out = out;
        this.tee = tee;
    }

    @Override
    public void write(int b) throws IOException
    {
        out.write(b);
        tee.write(b);
    }

    @Override
    public void write(byte[] b) throws IOException
    {
        out.write(b);
        tee.write(b);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException
    {
        out.write(b, off, len);
        tee.write(b, off, len);
    }

    @Override
    public void flush() throws IOException
    {
        out.flush();
        tee.flush();
    }

    @Override
    public void close() throws IOException
    {
        out.close();
        tee.close();
    }
}