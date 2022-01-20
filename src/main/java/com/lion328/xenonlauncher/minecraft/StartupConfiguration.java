// Copyright (C) 2022 Waritnan Sookbuntherng
// SPDX-License-Identifier: Apache-2.0

package com.lion328.xenonlauncher.minecraft;

import com.lion328.xenonlauncher.util.OperatingSystem;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class StartupConfiguration {

    private final OperatingSystem os;
    private final String osVersion;
    private final OperatingSystem.Architecture osArch;
    private final Map<String, Boolean> features;
    private final Map<String, String> variables;

    public StartupConfiguration(
            OperatingSystem os,
            String osVersion,
            OperatingSystem.Architecture osArch,
            Map<String, Boolean> features,
            Map<String, String> variables
    ) {
        this.os = os;
        this.osVersion = osVersion;
        this.osArch = osArch;

        if (features != null) {
            features = Collections.unmodifiableMap(new HashMap<>(features));
        }
        this.features = features;

        if (variables != null) {
            variables = Collections.unmodifiableMap(new HashMap<>(variables));
        }
        this.variables = variables;
    }

    public OperatingSystem getOperatingSystem() {
        return os;
    }

    public String getOperatingSystemVersion() {
        return osVersion;
    }

    public OperatingSystem.Architecture getArchitecture() {
        return osArch;
    }

    public Map<String, Boolean> getFeatures() {
        return features;
    }

    public Map<String, String> getVariables() {
        return variables;
    }

    public StartupConfiguration withVariables(Map<String, String> variables) {
        return new StartupConfiguration(os, osVersion, osArch, features, variables);
    }

    public static StartupConfiguration getRunningConfig() {
        return getRunningConfig(null);
    }

    public static StartupConfiguration getRunningConfig(Map<String, Boolean> features) {
        return new StartupConfiguration(
                OperatingSystem.getCurrentOS(),
                OperatingSystem.getCurrentVersion(),
                OperatingSystem.getCurrentArchitecture(),
                features,
                null
        );
    }
}
