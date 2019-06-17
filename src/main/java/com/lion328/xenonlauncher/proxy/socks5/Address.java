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
import java.net.InetAddress;

public class Address
{

    private final AddressType type;
    private final byte[] ipAddr;
    private final String domainName;
    private final byte[] bArray;

    public Address(AddressType type, byte[] ipAddr, String domainName) throws IOException
    {
        int ipLen = type == AddressType.IPV4 ? 4 : 16;
        switch (type)
        {
            case IPV4:
            case IPV6:
                if (ipAddr == null || ipAddr.length != ipLen)
                {
                    throw new IOException("Invalid IP address");
                }
                break;
            case DOMAINNAME:
                if (domainName == null || domainName.length() > 0xFF)
                {
                    throw new IOException("Invalid domain name");
                }
                break;
        }
        this.type = type;
        this.ipAddr = ipAddr;
        this.domainName = domainName;

        int i = 0;
        bArray = new byte[1 + (type == AddressType.DOMAINNAME ? domainName.length() + 1 : ipLen)];
        bArray[i++] = (byte) (type.getByte() & 0xFF);
        switch (type)
        {
            case IPV4:
            case IPV6:
                System.arraycopy(ipAddr, 0, bArray, i, ipLen);
                break;
            case DOMAINNAME:
                bArray[i++] = (byte) (domainName.length() & 0xFF);
                System.arraycopy(domainName.toCharArray(), 0, bArray, i, domainName.length());
        }
    }

    public static Address fromInetAddress(AddressType type, InetAddress address) throws IOException
    {
        byte[] ipAddr = null;
        String domainName = null;
        switch (type)
        {
            case IPV4:
            case IPV6:
                ipAddr = address.getAddress();
                break;
            case DOMAINNAME:
                domainName = address.getHostName();
                break;
        }
        return new Address(type, ipAddr, domainName);
    }

    public static Address fromInputStream(InputStream in) throws IOException
    {
        AddressType addressType = AddressType.getByByte(in.read());

        byte[] ip;
        switch (addressType)
        {
            case IPV4:
                ip = new byte[4];
                in.read(ip);
                return new Address(addressType, ip, null);
            case IPV6:
                ip = new byte[16];
                in.read(ip);
                return new Address(addressType, ip, null);
            case DOMAINNAME:
                int len = in.read() & 0xFF;
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < len; i++)
                {
                    sb.append((char) (in.read() & 0xFF));
                }
                return new Address(addressType, null, sb.toString());
        }

        return null;
    }

    public AddressType getType()
    {
        return type;
    }

    public byte[] getIPAddress()
    {
        return ipAddr.clone();
    }

    public String getDomainName()
    {
        return domainName;
    }

    public byte[] toByteArray()
    {
        return bArray.clone();
    }

    public InetAddress toInetAddress() throws IOException
    {
        switch (type)
        {
            case IPV4:
            case IPV6:
                return InetAddress.getByAddress(ipAddr.clone());
            case DOMAINNAME:
                return InetAddress.getByName(domainName);
        }
        return null;
    }
}
