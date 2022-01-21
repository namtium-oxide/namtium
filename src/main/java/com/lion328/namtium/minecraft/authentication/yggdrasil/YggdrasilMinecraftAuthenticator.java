// Copyright (C) 2016-2022 Waritnan Sookbuntherng
// SPDX-License-Identifier: Apache-2.0

package com.lion328.namtium.minecraft.authentication.yggdrasil;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.lion328.namtium.minecraft.authentication.MinecraftAuthenticator;
import com.lion328.namtium.minecraft.authentication.UserInformation;
import com.lion328.namtium.minecraft.authentication.exception.InvalidCredentialsException;
import com.lion328.namtium.minecraft.authentication.exception.MinecraftAuthenticatorException;
import com.lion328.namtium.minecraft.authentication.yggdrasil.exception.InvalidClientTokenException;
import com.lion328.namtium.minecraft.authentication.yggdrasil.exception.InvalidImplementationException;
import com.lion328.namtium.minecraft.authentication.yggdrasil.exception.YggdrasilAPIException;
import com.lion328.namtium.minecraft.authentication.yggdrasil.exception.YggdrasilErrorMessage;
import com.lion328.namtium.minecraft.authentication.yggdrasil.message.RefreshMessage;
import com.lion328.namtium.minecraft.authentication.yggdrasil.message.request.AuthenticateRequest;
import com.lion328.namtium.minecraft.authentication.yggdrasil.message.request.ValidateRequest;
import com.lion328.namtium.minecraft.authentication.yggdrasil.message.response.AuthenticateResponse;
import com.lion328.namtium.util.URLUtil;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.impl.DefaultBHttpClientConnection;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;

import javax.net.ssl.SSLSocketFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class YggdrasilMinecraftAuthenticator implements MinecraftAuthenticator {

    public static final URL DEFAULT_YGGDRASIL_SERVER = URLUtil.constantURL("https://authserver.mojang.com/");
    public static final GameAgent MINECRAFT_AGENT = new GameAgent("minecraft", 1);

    private static final Gson gson = new Gson();

    private final URL serverURL;
    private final String clientToken;

    private String accessToken;
    private UserProfile profile;
    private UserInformation userInfo;

    public YggdrasilMinecraftAuthenticator() {
        this(DEFAULT_YGGDRASIL_SERVER, null, generateClientToken());
    }

    public YggdrasilMinecraftAuthenticator(String accessToken, String clientToken) {
        this(DEFAULT_YGGDRASIL_SERVER, accessToken, clientToken);
    }

    public YggdrasilMinecraftAuthenticator(URL serverURL) {
        this(serverURL, null, generateClientToken());
    }

    public YggdrasilMinecraftAuthenticator(URL serverURL, String accessToken, String clientToken) {
        this.serverURL = serverURL;
        this.accessToken = accessToken;
        this.clientToken = clientToken;
    }

    public static String generateClientToken() {
        return UUID.randomUUID().toString();
    }

    @Override
    public void login(String username, char[] password) throws IOException, MinecraftAuthenticatorException {
        String passwordString = new String(password); // bad idea

        AuthenticateRequest request = new AuthenticateRequest(MINECRAFT_AGENT, username, passwordString, clientToken);
        AuthenticateResponse response = sendRequest("authenticate", request, AuthenticateResponse.class, true);

        if (response == null || response.getSelectedProfile() == null) {
            throw new InvalidCredentialsException("Minecraft profiles not found!");
        }

        checkClientToken(request.getClientToken(), response.getClientToken());

        accessToken = response.getAccessToken();
        profile = response.getSelectedProfile();

        updateUserInformation();
    }

    @Override
    public void logout() throws IOException, MinecraftAuthenticatorException {
        sendRequest("invalidate", new ValidateRequest(accessToken, clientToken), null, false);

        userInfo = null;
    }

    @Override
    public void refresh() throws IOException, MinecraftAuthenticatorException {
        RefreshMessage request = new RefreshMessage(accessToken, clientToken);
        RefreshMessage response = sendRequest("refresh", request, RefreshMessage.class, true);

        checkClientToken(request.getClientToken(), response.getClientToken());

        accessToken = response.getAccessToken();
        profile = response.getSelectedProfile();

        updateUserInformation();
    }

    @Override
    public UserInformation getUserInformation() {
        return userInfo;
    }

    @Override
    public String getPlayerName() {
        if (profile == null) {
            return null;
        }

        return profile.getName();
    }

    private void updateUserInformation() {
        if (profile == null) {
            userInfo = null;

            return;
        }

        userInfo = new UserInformation(profile.getId(), profile.getName(), accessToken, clientToken);
    }

    private void checkClientToken(String sent, String received) throws InvalidClientTokenException {
        if (!sent.equals(received)) {
            throw new InvalidClientTokenException();
        }
    }

    private ResponseState sendRequest(String endpoint, String data) throws IOException, YggdrasilAPIException {
        URL url = new URL(serverURL, endpoint);

        // HttpURLConnection can only handle 2xx response code for headers
        // so it need to use HttpCore instead
        // maybe I could use an alternative like HttpClient
        // but for lightweight, I think is not a good idea

        BasicHttpEntity entity = new BasicHttpEntity();

        byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
        entity.setContent(new ByteArrayInputStream(dataBytes));
        entity.setContentLength(dataBytes.length);

        HttpEntityEnclosingRequest request = new BasicHttpEntityEnclosingRequest("POST", url.getFile(), HttpVersion.HTTP_1_1);
        request.setHeader(new BasicHeader("Host", url.getHost()));
        request.setHeader(new BasicHeader("Content-Type", "application/json"));
        request.setHeader(new BasicHeader("Content-Length", Integer.toString(dataBytes.length)));

        request.setEntity(entity);

        Socket s;
        int port = url.getPort();

        if (url.getProtocol().equals("https")) {
            if (port == -1) {
                port = 443;
            }

            s = SSLSocketFactory.getDefault().createSocket(url.getHost(), port);
        } else {
            if (port == -1) {
                port = 80;
            }

            s = new Socket(url.getHost(), port);
        }

        DefaultBHttpClientConnection connection = new DefaultBHttpClientConnection(8192);
        connection.bind(s);

        try {
            connection.sendRequestHeader(request);
            connection.sendRequestEntity(request);

            HttpResponse response = connection.receiveResponseHeader();
            connection.receiveResponseEntity(response);

            if (!response.getFirstHeader("Content-Type").getValue().startsWith("application/json")) {
                throw new InvalidImplementationException("Invalid content type");
            }

            InputStream stream = response.getEntity().getContent();
            StringBuilder sb = new StringBuilder();
            int b;

            while ((b = stream.read()) != -1) {
                sb.append((char) b);
            }

            return new ResponseState(response.getStatusLine().getStatusCode(), sb.toString());
        } catch (HttpException e) {
            throw new IOException(e);
        }
    }

    private <T> T sendRequest(String endpoint, Object request, Class<T> clazz, boolean nullCheck) throws IOException, YggdrasilAPIException {
        String requestJson = gson.toJson(request);
        ResponseState state = sendRequest(endpoint, requestJson);

        try {
            gson.fromJson(state.getData(), Object.class);
        } catch (JsonParseException e) {
            throw new InvalidImplementationException();
        }

        if (state.getResponseCode() / 100 != 2) {
            YggdrasilErrorMessage message = gson.fromJson(state.getData(), YggdrasilErrorMessage.class);
            throw message.toException();
        }

        if (state.getResponseCode() == HttpURLConnection.HTTP_NO_CONTENT || state.getData().length() == 0) {
            if (nullCheck) {
                throw new InvalidImplementationException("Get empty response: " + endpoint);
            }

            return null;
        }

        if (clazz == null) {
            return null;
        }

        T ret = gson.fromJson(state.getData(), clazz);

        if (ret == null) {
            throw new InvalidImplementationException("GSON returns null");
        }

        return ret;
    }

    public static class ResponseState {

        private final int responseCode;
        private final String data;

        public ResponseState(int responseCode, String data) {
            this.responseCode = responseCode;
            this.data = data;
        }

        public int getResponseCode() {
            return responseCode;
        }

        public String getData() {
            return data;
        }
    }
}
