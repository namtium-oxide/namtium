// Copyright (C) 2016-2022 Waritnan Sookbuntherng
// SPDX-License-Identifier: Apache-2.0

package com.lion328.xenonlauncher.minecraft.api.authentication.yggdrasil.message.request;

import com.google.gson.annotations.SerializedName;

public class SignoutRequest
{

    @SerializedName("username")
    private String username;
    @SerializedName("password")
    private String password;

    public SignoutRequest()
    {

    }

    public SignoutRequest(String username, String password)
    {
        this.username = username;
        this.password = password;
    }

    public String getUsername()
    {
        return username;
    }

    public String getPassword()
    {
        return password;
    }
}
