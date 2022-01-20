// Copyright (C) 2016-2022 Waritnan Sookbuntherng
// SPDX-License-Identifier: Apache-2.0

package com.lion328.xenonlauncher.minecraft.api.authentication.yggdrasil.message.response;

import com.google.gson.annotations.SerializedName;
import com.lion328.xenonlauncher.minecraft.api.authentication.yggdrasil.UserProfile;
import com.lion328.xenonlauncher.minecraft.api.authentication.yggdrasil.UserProperties;

import java.util.List;

public class AuthenticateResponse {

    @SerializedName("accessToken")
    private String accessToken;
    @SerializedName("clientToken")
    private String clientToken;
    @SerializedName("availableProfiles")
    private List<UserProfile> availableProfiles;
    @SerializedName("selectedProfile")
    private UserProfile profile;
    @SerializedName("user")
    private UserProperties user;

    public AuthenticateResponse() {

    }

    public AuthenticateResponse(String accessToken, String clientToken, List<UserProfile> availableProfiles, UserProfile profile) {
        this(accessToken, clientToken, availableProfiles, profile, null);
    }

    public AuthenticateResponse(String accessToken, String clientToken, List<UserProfile> availableProfiles, UserProfile profile, UserProperties user) {
        this.accessToken = accessToken;
        this.clientToken = clientToken;
        this.availableProfiles = availableProfiles;
        this.profile = profile;
        this.user = user;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getClientToken() {
        return clientToken;
    }

    public List<UserProfile> getAvailableProfiles() {
        return availableProfiles;
    }

    public UserProfile getSelectedProfile() {
        return profile;
    }

    public UserProperties getUser() {
        return user;
    }
}
