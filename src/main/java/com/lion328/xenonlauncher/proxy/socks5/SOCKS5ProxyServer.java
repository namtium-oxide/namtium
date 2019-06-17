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

import com.lion328.xenonlauncher.proxy.DataHandler;
import com.lion328.xenonlauncher.proxy.ProxyServer;
import com.lion328.xenonlauncher.proxy.util.BufferedSocket;
import com.lion328.xenonlauncher.proxy.util.StreamUtil;
import com.lion328.xenonlauncher.settings.LauncherConstant;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Map;
import java.util.TreeMap;

public class SOCKS5ProxyServer implements ProxyServer
{

    public static final int VERSION = 0x05;

    private boolean running;
    private ServerSocket serverSocket;
    private TreeMap<Integer, DataHandler> handlers;

    public SOCKS5ProxyServer()
    {
        running = false;
        handlers = new TreeMap<>();
    }

    private void processRequest(Socket socket) throws Exception
    {
        InputStream in = socket.getInputStream();
        OutputStream out = socket.getOutputStream();

        GreetingRequestPacket greetingReq = new GreetingRequestPacket();
        greetingReq.read(in);

        if (greetingReq.getVersion() != VERSION)
        {
            throw new IOException("Version mismatch");
        }

        AuthenticationMethod noAuth = null;
        for (AuthenticationMethod method : greetingReq.getAvailableAuthenticationMethods())
        {
            if (method == AuthenticationMethod.NO_AUTHENTICATION_REQUIRED)
            {
                noAuth = method;
                break;
            }
        }

        if (noAuth == null)
        {
            throw new IOException("No supported authentication method available");
        }

        new GreetingResponsePacket(VERSION, noAuth).write(out);

        ConnectRequestPacket connectReq = new ConnectRequestPacket();
        connectReq.read(in);

        if (connectReq.getVersion() != VERSION)
        {
            throw new IOException("Version mismatch");
        }

        if (connectReq.getAddress().getType() == AddressType.IPV6)
        {
            new ConnectResponsePacket(VERSION, ConnectResponsePacket.State.ADDRESS_TYPE_NOT_SUPPORTED,
                    connectReq.getAddress(), connectReq.getPort()).write(out);
            return;
        }

        switch (connectReq.getCommand())
        {
            case CONNECT:
                handleConnect(connectReq, socket);
                break;
            case BIND:
                handleBind(connectReq, socket);
                break;
            case UDP_ASSOCIATE:
            default:
                new ConnectResponsePacket(VERSION, ConnectResponsePacket.State.COMMAND_NOT_SUPPORTED,
                        connectReq.getAddress(), connectReq.getPort()).write(out);
                break;
        }
    }

    private void handleConnect(ConnectRequestPacket connectReq, Socket socket) throws Exception
    {
        InetAddress addr = connectReq.getAddress().toInetAddress();
        Socket connSocket = new BufferedSocket(new Socket(addr, connectReq.getPort()));

        new ConnectResponsePacket(VERSION, ConnectResponsePacket.State.SUCCEEDED,
                Address.fromInetAddress(AddressType.IPV4, socket.getInetAddress()), socket.getPort()).write(
                socket.getOutputStream());

        for (Map.Entry<Integer, DataHandler> entry : handlers.entrySet())
        {
            if (entry.getValue().process(socket, connSocket))
            {
                break;
            }
        }

        connSocket.close();
    }

    private void handleBind(ConnectRequestPacket connectReq, Socket socket) throws IOException
    {
        Socket connSocket;
        InetAddress targetAddress = connectReq.getAddress().toInetAddress();
        ServerSocket server = new ServerSocket(0, 10, InetAddress.getLocalHost());
        server.setSoTimeout(10000);

        new ConnectResponsePacket(VERSION, ConnectResponsePacket.State.SUCCEEDED,
                Address.fromInetAddress(AddressType.IPV4, server.getInetAddress()), server.getLocalPort()).write(
                socket.getOutputStream());

        while (true)
        {
            connSocket = null;
            try
            {
                connSocket = new BufferedSocket(server.accept());
            }
            catch (SocketTimeoutException e)
            {
                break;
            }
            if (connSocket.getInetAddress().equals(targetAddress) && connSocket.getPort() == connectReq.getPort())
            {
                server.close();
                break;
            }
            else
            {
                connSocket.close();
            }
        }

        ConnectResponsePacket.State state = connSocket == null ? ConnectResponsePacket.State.TTL_EXPIRED : ConnectResponsePacket.State.SUCCEEDED;
        new ConnectResponsePacket(VERSION, state,
                Address.fromInetAddress(AddressType.IPV4, connSocket.getInetAddress()), connSocket.getPort()).write(
                socket.getOutputStream());

        StreamUtil.pipeStreamThread(socket.getInputStream(), connSocket.getOutputStream());
        StreamUtil.pipeStream(connSocket.getInputStream(), socket.getOutputStream());

        connSocket.close();
    }

    @Override
    public void start(ServerSocket server) throws IOException
    {
        if (running)
        {
            return;
        }
        running = true;
        serverSocket = server;
        while (running)
        {
            final Socket socket = new BufferedSocket(serverSocket.accept());
            new Thread()
            {

                public void run()
                {
                    try
                    {
                        processRequest(socket);
                        socket.close();
                    }
                    catch (Exception e)
                    {
                        LauncherConstant.LOGGER.catching(e);
                    }
                }
            }.start();
        }
    }

    @Override
    public void stop()
    {
        running = false;
    }

    @Override
    public void addDataHandler(int level, DataHandler handler)
    {
        handlers.put(level, handler);
    }
}
