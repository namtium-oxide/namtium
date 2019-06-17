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

public class ConnectRequestPacket implements Packet
{

    private int version;
    private Command command;
    private Address address;
    private int port;

    public ConnectRequestPacket()
    {

    }

    public ConnectRequestPacket(int version, Command command, Address address, int port) throws IOException
    {
        this.version = version;
        this.command = command;
        this.address = address;
    }

    public int getVersion()
    {
        return version;
    }

    public void setVersion(int version)
    {
        this.version = version;
    }

    public Command getCommand()
    {
        return command;
    }

    public void setCommand(Command command)
    {
        this.command = command;
    }

    public Address getAddress()
    {
        return address;
    }

    public void setAddress(Address address)
    {
        this.address = address;
    }

    public int getPort()
    {
        return port;
    }

    public void setPort(int port)
    {
        this.port = port;
    }

    @Override
    public void read(InputStream in) throws IOException
    {
        version = in.read() & 0xFF;
        command = Command.getByByte(in.read());
        in.read();
        address = Address.fromInputStream(in);
        port = ((in.read() & 0xFF) << 8) | (in.read() & 0xFF);
    }

    @Override
    public void write(OutputStream out) throws IOException
    {
        out.write(version & 0xFF);
        out.write(command.getByte() & 0xFF);
        out.write(0x00);
        out.write(address.toByteArray());
        out.write((port >> 8) & 0xFF);
        out.write(port & 0xFF);
    }

    public enum Command
    {

        CONNECT(0x01), BIND(0x02), UDP_ASSOCIATE(0x03);

        private int b;

        Command(int b)
        {
            this.b = b;
        }

        public static Command getByByte(int b)
        {
            for (Command cmd : values())
            {
                if (cmd.b == (b & 0xFF))
                {
                    return cmd;
                }
            }
            return null;
        }

        public int getByte()
        {
            return b & 0xFF;
        }
    }
}
