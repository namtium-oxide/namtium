// Copyright (C) 2016-2022 Waritnan Sookbuntherng
// SPDX-License-Identifier: Apache-2.0

package com.lion328.namtium.launcher;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class Language {

    private static Map<String, String> table;

    public static String get(String key) {
        initializeTable();

        String ret = table.get(key);

        if (ret != null) {
            return ret;
        }

        return key;
    }

    public static void extend(Map<String, String> map) {
        initializeTable();

        table.putAll(map);
    }

    public static void initializeTable() {
        if (table == null) {
            Reader reader = new InputStreamReader(
                    Language.class.getResourceAsStream("/com/lion328/namtium/launcher/hydra/lang.json"),
                    StandardCharsets.UTF_8
            );
            table = new Gson().fromJson(reader, new TypeToken<Map<String, String>>() {}.getType());
        }
    }
}
