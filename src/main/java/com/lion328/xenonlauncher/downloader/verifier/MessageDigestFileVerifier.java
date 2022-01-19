/*
 * Copyright (c) 2018 Waritnan Sookbuntherng
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.lion328.xenonlauncher.downloader.verifier;

import com.lion328.xenonlauncher.util.ByteUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class MessageDigestFileVerifier implements FileVerifier
{

    public static final String MD2 = "MD2";
    public static final String MD5 = "MD5";
    public static final String SHA_1 = "SHA-1";
    public static final String SHA_256 = "SHA-256";
    public static final String SHA_384 = "SHA-384";
    public static final String SHA_512 = "SHA-512";

    private final String algorithm;
    private final byte[] hash;

    public MessageDigestFileVerifier(String algorithm, String hash)
    {
        this(algorithm, ByteUtil.fromHexString(hash));
    }

    public MessageDigestFileVerifier(String algorithm, byte[] hash)
    {
        this.algorithm = algorithm;
        this.hash = hash.clone();
    }

    public byte[] getFileHash(File file) throws IOException
    {
        if (!file.exists())
        {
            return null;
        }

        MessageDigest md;

        try
        {
            md = MessageDigest.getInstance(algorithm);
        }
        catch (NoSuchAlgorithmException e)
        {
            throw new RuntimeException(e);
        }

        InputStream in = new DigestInputStream(new FileInputStream(file), md);

        byte[] buffer = new byte[8192];

        while (true)
        {
            if (in.read(buffer) == -1)
            {
                break;
            }
        }

        in.close();

        return md.digest();
    }

    @Override
    public boolean isValid(File file) throws IOException
    {
        return Arrays.equals(getFileHash(file), hash);
    }
}
