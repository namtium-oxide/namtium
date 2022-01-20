package com.lion328.xenonlauncher.minecraft.downloader;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameVersionList
{

    public static final URL MANIFEST_URL;

    private final Map<String, URL> versions;

    public GameVersionList(Map<String, URL> versions)
    {
        this.versions = versions;
    }

    public URL getVersionManifestURL(String version)
    {
        return versions.get(version);
    }

    public static GameVersionList getVersionsFromMojang() throws IOException
    {
        HttpURLConnection conn = (HttpURLConnection) MANIFEST_URL.openConnection();
        if (conn.getResponseCode() != 200)
        {
            throw new IOException("Server returns " + conn.getResponseCode());
        }

        Gson gson = new Gson();
        Reader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        JsonObject obj = gson.fromJson(reader, JsonObject.class);
        List<VersionInfo> versions = gson.fromJson(obj.get("versions"),
                new TypeToken<List<VersionInfo>>() {}.getType());

        Map<String, URL> result = new HashMap<>();
        for (VersionInfo v : versions)
        {
            result.put(v.id, v.url);
        }

        return new GameVersionList(result);
    }

    private static class VersionInfo {
        @SerializedName("id")
        public String id;
        @SerializedName("url")
        public URL url;
    }

    static {
        try {
            MANIFEST_URL = new URL("https://launchermeta.mojang.com/mc/game/version_manifest.json");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
