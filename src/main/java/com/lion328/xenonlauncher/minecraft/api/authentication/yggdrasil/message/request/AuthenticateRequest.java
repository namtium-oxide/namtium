// Copyright (C) 2016-2022 Waritnan Sookbuntherng
// SPDX-License-Identifier: Apache-2.0

package com.lion328.xenonlauncher.minecraft.api.authentication.yggdrasil.message.request;

import com.google.gson.annotations.SerializedName;
import com.lion328.xenonlauncher.minecraft.api.authentication.yggdrasil.GameAgent;

public class AuthenticateRequest
{

    @SerializedName("agent")
    private GameAgent agent;
    @SerializedName("username")
    private String username;
    @SerializedName("password")
    private String password;
    @SerializedName("clientToken")
    private String clientToken;
    @SerializedName("requestUser")
    private boolean requestUser = true;

    public AuthenticateRequest()
    {

    }

    public AuthenticateRequest(GameAgent agent, String username, String password, String clientToken)
    {
        this(agent, username, password, clientToken, true);
    }

    public AuthenticateRequest(GameAgent agent, String username, String password, String clientToken, boolean requestUser)
    {
        this.agent = agent;
        this.username = username;
        this.password = password;
        this.clientToken = clientToken;
        this.requestUser = requestUser;
    }

    public GameAgent getAgent()
    {
        return agent;
    }

    public String getUsername()
    {
        return username;
    }

    public String getPassword()
    {
        return password;
    }

    public String getClientToken()
    {
        return clientToken;
    }
}
