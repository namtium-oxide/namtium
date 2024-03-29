// Copyright (C) 2022 Waritnan Sookbuntherng
// SPDX-License-Identifier: Apache-2.0

package com.lion328.namtium.minecraft.manifest;

import com.google.gson.annotations.SerializedName;
import com.lion328.namtium.minecraft.StartupConfiguration;
import com.lion328.namtium.util.OperatingSystem;

import java.util.Collections;
import java.util.Map;

public class Rule {
    @SerializedName("action")
    private RuleAction action;
    @SerializedName("os")
    private OperatingSystemIdentifier osIdentifier;
    @SerializedName("features")
    private Map<String, Boolean> features;

    public Rule() {

    }

    public RuleAction getAction() {
        return action;
    }

    public boolean isAllowAction() {
        return action == RuleAction.ALLOW;
    }

    public Map<String, Boolean> getFeatures() {
        return features == null ? null : Collections.unmodifiableMap(features);
    }

    public boolean isMatched(StartupConfiguration config) {
        if (config == null) {
            return false;
        }

        return isMatchedOnOS(config.getOperatingSystem(), config.getOperatingSystemVersion()) &&
                isMatchedOnFeatures(config.getFeatures());
    }

    private boolean isMatchedOnOS(OperatingSystem os, String version) {
        return osIdentifier == null || osIdentifier.isMatch(os, version);
    }

    private boolean isMatchedOnFeatures(Map<String, Boolean> targetFeatures) {
        if (features == null) {
            return true;
        }

        if (targetFeatures == null || targetFeatures.isEmpty()) {
            return features.isEmpty();
        }

        // TODO: verify the behavior on the official launcher
        for (Map.Entry<String, Boolean> entry : features.entrySet()) {
            if (targetFeatures.containsKey(entry.getKey())) {
                if (entry.getValue() != targetFeatures.get(entry.getKey())) {
                    return false;
                }
            }
        }

        return true;
    }

    public static boolean isAllowed(Iterable<Rule> rules, StartupConfiguration config) {
        if (rules == null) {
            return true;
        }

        boolean allow = false;

        for (Rule rule : rules) {
            if (rule.isMatched(config)) {
                allow = rule.isAllowAction();
            }
        }

        return allow;
    }
}
