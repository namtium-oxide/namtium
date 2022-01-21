// Copyright (C) 2022 Waritnan Sookbuntherng
// SPDX-License-Identifier: Apache-2.0

package com.lion328.namtium.launcher.hydra.config;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

public class DebugHydraConfiguration implements HydraConfiguration {
    private final HydraConfiguration inner;

    private DebugHydraConfiguration(HydraConfiguration inner) {
        this.inner = inner;
    }

    public static HydraConfiguration convert(HydraConfiguration inner) {
        if (!isDebugEnabled() || inner instanceof DebugHydraConfiguration) {
            return inner;
        }
        return new DebugHydraConfiguration(inner);
    }

    private static boolean isDebugEnabled() {
        return "true".equals(System.getProperty("com.lion328.hydra.debugEnabled"));
    }

    private static String getProperty(String key) {
        return System.getProperty("com.lion328.hydra." + key);
    }

    private static URL getPropertyAsURL(String key) {
        String value = getProperty(key);
        try {
            return value == null ? null : new URL(value);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public URL getRegisterURL() {
        URL url = getPropertyAsURL("registerURL");
        return url == null ? inner.getRegisterURL() : url;
    }

    @Override
    public URL getAuthenticationURL() {
        URL url = getPropertyAsURL("authURL");
        return url == null ? inner.getAuthenticationURL() : url;
    }

    @Override
    public URL getWhitelistFileListURL() {
        URL url = getPropertyAsURL("whitelistURL");
        return url == null ? inner.getWhitelistFileListURL() : url;
    }

    @Override
    public URL getCompressedFileListURL() {
        URL url = getPropertyAsURL("fileListURL");
        return url == null ? inner.getCompressedFileListURL() : url;
    }

    @Override
    public URL getFileBaseURL() {
        URL url = getPropertyAsURL("fileBaseURL");
        return url == null ? inner.getFileBaseURL() : url;
    }

    @Override
    public UUID getApplicationUUID() {
        String v = getProperty("appUUID");
        return v == null ? inner.getApplicationUUID() : UUID.fromString(v);
    }

    @Override
    public String getGameVersionName() {
        String v = getProperty("gameVersionName");
        return v == null ? inner.getGameVersionName() : getProperty("gameVersionName");
    }

    @Override
    public String[] getVMArguments() {
        return inner.getVMArguments();
    }

    @Override
    public boolean streamGameOutput() {
        return inner.streamGameOutput();
    }

    @Override
    public boolean passPasswordToGame() {
        return inner.passPasswordToGame();
    }

    @Override
    public int getMinimumOfMaximumMemoryInMiB() {
        return inner.getMinimumOfMaximumMemoryInMiB();
    }

    @Override
    public int getDefaultMaximumMemoryInMiB() {
        return inner.getDefaultMaximumMemoryInMiB();
    }
}
