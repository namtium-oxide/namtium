// Copyright (C) 2016-2022 Waritnan Sookbuntherng
// SPDX-License-Identifier: Apache-2.0

package com.lion328.namtium.minecraft.authentication;

import com.lion328.namtium.minecraft.authentication.exception.MinecraftAuthenticatorException;

import java.io.IOException;

public interface MinecraftAuthenticator {

    void login(String username, char[] password) throws IOException, MinecraftAuthenticatorException;

    void logout() throws IOException, MinecraftAuthenticatorException;

    void refresh() throws IOException, MinecraftAuthenticatorException;

    UserInformation getUserInformation();

    String getPlayerName();
}
