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

package com.lion328.xenonlauncher.minecraft.api.authentication.yggdrasil.message.response;

import com.google.gson.annotations.SerializedName;
import com.lion328.xenonlauncher.minecraft.api.authentication.yggdrasil.UserProfile;
import com.lion328.xenonlauncher.minecraft.api.authentication.yggdrasil.UserProperties;

import java.util.List;

public class AuthenticateResponse
{

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

    public AuthenticateResponse()
    {

    }

    public AuthenticateResponse(String accessToken, String clientToken, List<UserProfile> availableProfiles, UserProfile profile)
    {
        this(accessToken, clientToken, availableProfiles, profile, null);
    }

    public AuthenticateResponse(String accessToken, String clientToken, List<UserProfile> availableProfiles, UserProfile profile, UserProperties user)
    {
        this.accessToken = accessToken;
        this.clientToken = clientToken;
        this.availableProfiles = availableProfiles;
        this.profile = profile;
        this.user = user;
    }

    public String getAccessToken()
    {
        return accessToken;
    }

    public String getClientToken()
    {
        return clientToken;
    }

    public List<UserProfile> getAvailableProfiles()
    {
        return availableProfiles;
    }

    public UserProfile getSelectedProfile()
    {
        return profile;
    }

    public UserProperties getUser()
    {
        return user;
    }
}
