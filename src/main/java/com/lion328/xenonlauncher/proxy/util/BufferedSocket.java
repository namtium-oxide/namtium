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

package com.lion328.xenonlauncher.proxy.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.channels.SocketChannel;

public class BufferedSocket extends Socket
{

    private Socket socket;
    private BufferedInputStream in = null;

    public BufferedSocket(Socket socket)
    {
        this.socket = socket;
    }

    @Override
    public void connect(SocketAddress endpoint) throws IOException
    {
        socket.connect(endpoint);
    }

    @Override
    public void connect(SocketAddress endpoint, int timeout) throws IOException
    {
        socket.connect(endpoint, timeout);
    }

    @Override
    public void bind(SocketAddress bindpoint) throws IOException
    {
        socket.bind(bindpoint);
    }

    @Override
    public InetAddress getInetAddress()
    {
        return socket.getInetAddress();
    }

    @Override
    public InetAddress getLocalAddress()
    {
        return socket.getLocalAddress();
    }

    @Override
    public int getPort()
    {
        return socket.getPort();
    }

    @Override
    public int getLocalPort()
    {
        return socket.getLocalPort();
    }

    @Override
    public SocketAddress getRemoteSocketAddress()
    {
        return socket.getRemoteSocketAddress();
    }

    @Override
    public SocketAddress getLocalSocketAddress()
    {
        return socket.getLocalSocketAddress();
    }

    @Override
    public SocketChannel getChannel()
    {
        return socket.getChannel();
    }

    @Override
    public synchronized InputStream getInputStream() throws IOException
    {
        if (in == null)
        {
            in = new BufferedInputStream(socket.getInputStream(), 0xFFFF);
        }
        return in;
    }

    @Override
    public OutputStream getOutputStream() throws IOException
    {
        return socket.getOutputStream();
    }

    @Override
    public boolean getTcpNoDelay() throws SocketException
    {
        return socket.getTcpNoDelay();
    }

    @Override
    public void setTcpNoDelay(boolean on) throws SocketException
    {
        socket.setTcpNoDelay(on);
    }

    @Override
    public void setSoLinger(boolean on, int linger) throws SocketException
    {
        socket.setSoLinger(on, linger);
    }

    @Override
    public int getSoLinger() throws SocketException
    {
        return socket.getSoLinger();
    }

    @Override
    public void sendUrgentData(int data) throws IOException
    {
        socket.sendUrgentData(data);
    }

    @Override
    public boolean getOOBInline() throws SocketException
    {
        return socket.getOOBInline();
    }

    @Override
    public void setOOBInline(boolean on) throws SocketException
    {
        socket.setOOBInline(on);
    }

    @Override
    public synchronized int getSoTimeout() throws SocketException
    {
        return socket.getSoTimeout();
    }

    @Override
    public synchronized void setSoTimeout(int timeout) throws SocketException
    {
        socket.setSoTimeout(timeout);
    }

    @Override
    public synchronized int getSendBufferSize() throws SocketException
    {
        return socket.getSendBufferSize();
    }

    @Override
    public synchronized void setSendBufferSize(int size) throws SocketException
    {
        socket.setSendBufferSize(size);
    }

    @Override
    public synchronized int getReceiveBufferSize() throws SocketException
    {
        return socket.getReceiveBufferSize();
    }

    @Override
    public synchronized void setReceiveBufferSize(int size) throws SocketException
    {
        socket.setReceiveBufferSize(size);
    }

    @Override
    public boolean getKeepAlive() throws SocketException
    {
        return socket.getKeepAlive();
    }

    @Override
    public void setKeepAlive(boolean on) throws SocketException
    {
        socket.setKeepAlive(on);
    }

    @Override
    public int getTrafficClass() throws SocketException
    {
        return socket.getTrafficClass();
    }

    @Override
    public void setTrafficClass(int tc) throws SocketException
    {
        socket.setTrafficClass(tc);
    }

    @Override
    public boolean getReuseAddress() throws SocketException
    {
        return socket.getReuseAddress();
    }

    @Override
    public void setReuseAddress(boolean on) throws SocketException
    {
        socket.setReuseAddress(on);
    }

    @Override
    public synchronized void close() throws IOException
    {
        socket.close();
    }

    @Override
    public void shutdownInput() throws IOException
    {
        socket.shutdownInput();
    }

    @Override
    public void shutdownOutput() throws IOException
    {
        socket.shutdownOutput();
    }

    @Override
    public String toString()
    {
        return socket.toString();
    }

    @Override
    public boolean isConnected()
    {
        return socket.isConnected();
    }

    @Override
    public boolean isBound()
    {
        return socket.isBound();
    }

    @Override
    public boolean isClosed()
    {
        return socket.isClosed();
    }

    @Override
    public boolean isInputShutdown()
    {
        return socket.isInputShutdown();
    }

    @Override
    public boolean isOutputShutdown()
    {
        return socket.isOutputShutdown();
    }

    @Override
    public void setPerformancePreferences(int connectionTime, int latency, int bandwidth)
    {
        socket.setPerformancePreferences(connectionTime, latency, bandwidth);
    }
}
