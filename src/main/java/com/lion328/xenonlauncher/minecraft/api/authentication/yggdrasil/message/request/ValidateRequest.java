// Copyright (C) 2016-2022 Waritnan Sookbuntherng
// SPDX-License-Identifier: Apache-2.0

package com.lion328.xenonlauncher.minecraft.api.authentication.yggdrasil.message.request;

import com.google.gson.annotations.SerializedName;

public class ValidateRequest
{

    @SerializedName("accessToken")
    private String accessToken;
    @SerializedName("clientToken")
    private String clientToken;

    public ValidateRequest()
    {

    }

    public ValidateRequest(String accessToken, String clientToken)
    {
        this.accessToken = accessToken;
        this.clientToken = clientToken;
    }

    public String getAccessToken()
    {
        return accessToken;
    }

    public String getClientToken()
    {
        return clientToken;
    }
}
