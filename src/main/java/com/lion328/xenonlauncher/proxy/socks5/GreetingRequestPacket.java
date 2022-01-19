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

package com.lion328.xenonlauncher.proxy.socks5;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class GreetingRequestPacket implements Packet
{

    private int version;
    private AuthenticationMethod[] availableAuthenticationMethods;

    public GreetingRequestPacket()
    {

    }

    public GreetingRequestPacket(int version, AuthenticationMethod[] availableAuthenticationMethods) throws IOException
    {
        this.version = version;
        this.availableAuthenticationMethods = availableAuthenticationMethods.clone();
    }

    public int getVersion()
    {
        return version;
    }

    public void setVersion(int version)
    {
        this.version = version;
    }

    public AuthenticationMethod[] getAvailableAuthenticationMethods()
    {
        return availableAuthenticationMethods;
    }

    public void setAvailableAuthenticationMethods(AuthenticationMethod[] availableAuthenticationMethods)
    {
        this.availableAuthenticationMethods = availableAuthenticationMethods;
    }

    @Override
    public void read(InputStream in) throws IOException
    {
        version = in.read() & 0xFF;
        int methodAvailable = in.read();
        availableAuthenticationMethods = new AuthenticationMethod[methodAvailable];
        for (int i = 0; i < methodAvailable; i++)
        {
            availableAuthenticationMethods[i] = AuthenticationMethod.getByByte((byte) (in.read() & 0xFF));
        }
    }

    @Override
    public void write(OutputStream out) throws IOException
    {
        out.write(version & 0xFF);
        out.write(availableAuthenticationMethods.length & 0xFF);
        for (AuthenticationMethod method : availableAuthenticationMethods)
        {
            out.write(method.getByte());
        }
    }
}
