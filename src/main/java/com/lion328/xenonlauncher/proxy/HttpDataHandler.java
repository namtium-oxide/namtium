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

package com.lion328.xenonlauncher.proxy;

import org.apache.http.ConnectionClosedException;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.ProtocolException;
import org.apache.http.impl.DefaultBHttpClientConnection;
import org.apache.http.impl.DefaultBHttpServerConnection;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpCoreContext;
import org.apache.http.protocol.HttpRequestHandler;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Map;
import java.util.TreeMap;

public class HttpDataHandler implements DataHandler
{

    public static final HttpRequestHandler STREAM_HANDLER = new HttpRequestHandler()
    {

        @Override
        public void handle(HttpRequest httpRequest, HttpResponse httpResponse, HttpContext httpContext) throws HttpException, IOException
        {
            if (httpContext.getAttribute("request.set") != Boolean.TRUE)
            {
                httpContext.setAttribute("response.need-original", true);
            }
        }
    };

    public static final int SOCKET_TIMEOUT = 60 * 1000;

    private final int timeout;
    private Map<Integer, HttpRequestHandler> handlers = new TreeMap<>();

    public HttpDataHandler()
    {
        this(SOCKET_TIMEOUT);
    }

    public HttpDataHandler(int timeout)
    {
        this.timeout = timeout;
    }

    public void addHttpRequestHandler(int level, HttpRequestHandler handler)
    {
        handlers.put(level, handler);
    }

    @Override
    public boolean process(Socket client, Socket server) throws Exception
    {
        InputStream clientIn = client.getInputStream();
        clientIn.mark(65536);

        try
        {
            DefaultBHttpServerConnection httpClient = new DefaultBHttpServerConnection(8192);
            httpClient.bind(client);
            httpClient.setSocketTimeout(timeout);

            DefaultBHttpClientConnection httpServer = new DefaultBHttpClientConnection(8192);
            httpServer.bind(server);

            HttpCoreContext context = HttpCoreContext.create();
            context.setAttribute("client.socket", client);
            context.setAttribute("server.socket", server);

            HttpEntityEnclosingRequest request;

            do
            {
                HttpRequest rawRequest = httpClient.receiveRequestHeader();

                if (rawRequest instanceof HttpEntityEnclosingRequest)
                {
                    request = (HttpEntityEnclosingRequest) rawRequest;
                }
                else
                {
                    request = new BasicHttpEntityEnclosingRequest(rawRequest.getRequestLine());
                    request.setHeaders(rawRequest.getAllHeaders());
                }

                httpClient.receiveRequestEntity(request);

                HttpResponse response = new BasicHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "OK"));

                boolean sent = false;

                for (Map.Entry<Integer, HttpRequestHandler> entry : handlers.entrySet())
                {
                    entry.getValue().handle(request, response, context);

                    if (context.getAttribute("response.set") instanceof HttpResponse)
                    {
                        response = (HttpResponse) context.getAttribute("response.set");
                    }

                    if (context.getAttribute("pipeline.end") == Boolean.TRUE)
                    {
                        break;
                    }

                    if (context.getAttribute("response.need-original") == Boolean.TRUE && !sent)
                    {
                        httpServer.sendRequestHeader(request);
                        httpServer.sendRequestEntity(request);
                        response = httpServer.receiveResponseHeader();
                        httpServer.receiveResponseEntity(response);

                        entry.getValue().handle(request, response, context);

                        context.removeAttribute("response.need-original");
                        context.setAttribute("request.sent", true);

                        sent = true;
                    }
                }

                if (context.getAttribute("response.sent") != Boolean.TRUE)
                {
                    httpClient.sendResponseHeader(response);

                    if (response.getEntity() != null)
                    {
                        httpClient.sendResponseEntity(response);
                    }
                }
            }
            while (request.getFirstHeader("Connection").getValue().equals("keep-alive"));

            return true;
        }
        catch (ProtocolException e)
        {
            clientIn.reset();
            return false;
        }
        catch (ConnectionClosedException e)
        {
            return true;
        }
    }
}
