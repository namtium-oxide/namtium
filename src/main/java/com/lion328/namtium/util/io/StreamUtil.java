// Copyright (C) 2016-2022 Waritnan Sookbuntherng
// SPDX-License-Identifier: Apache-2.0

package com.lion328.namtium.util.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class StreamUtil {

    public static void pipeStream(InputStream in, OutputStream out) {
        int b;
        try {
            while ((b = in.read()) != -1) {
                out.write(b);
            }
        } catch (IOException e) {
            try {
                out.close();
            } catch (IOException ignore) {

            }
        }
    }

    public static void pipeStreamThread(final InputStream in, final OutputStream out) {
        new Thread() {

            public void run() {
                pipeStream(in, out);
            }
        }.start();
    }
}
