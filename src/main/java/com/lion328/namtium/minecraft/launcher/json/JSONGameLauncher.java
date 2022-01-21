// Copyright (C) 2016-2022 Waritnan Sookbuntherng
// SPDX-License-Identifier: Apache-2.0

package com.lion328.namtium.minecraft.launcher.json;

import com.google.gson.Gson;
import com.lion328.namtium.downloader.repository.DependencyName;
import com.lion328.namtium.minecraft.StartupConfiguration;
import com.lion328.namtium.minecraft.authentication.UserInformation;
import com.lion328.namtium.minecraft.authentication.yggdrasil.UserProperties;
import com.lion328.namtium.minecraft.assets.VirtualAssetsInstaller;
import com.lion328.namtium.minecraft.assets.Assets;
import com.lion328.namtium.minecraft.launcher.BasicGameLauncher;
import com.lion328.namtium.minecraft.manifest.ExtractConfiguration;
import com.lion328.namtium.minecraft.manifest.GameLibrary;
import com.lion328.namtium.minecraft.manifest.GameVersion;
import com.lion328.namtium.minecraft.launcher.json.exception.LauncherVersionException;
import com.lion328.namtium.settings.LauncherConstant;
import com.lion328.namtium.util.OperatingSystem;
import com.lion328.namtium.util.io.FileUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class JSONGameLauncher extends BasicGameLauncher {

    private final List<String> customJvmArgs = new ArrayList<>();
    private final List<String> customGameArgs = new ArrayList<>();
    private final Map<String, String> variables = new HashMap<>();
    private final Map<String, Boolean> features = new HashMap<>();

    private boolean allowNativesArchFallback;
    private GameVersion versionInfo;
    private File basepathDir;
    private File librariesDir;
    private File versionsDir;
    private File gameDir;
    private File assetsDir;
    private File assetsIndexesDir;
    private File assetsObjectsDir;
    private File virtualAssetsDir;
    private File gameVirtualAssetsDir;

    public JSONGameLauncher(GameVersion version, File basepathDir) throws LauncherVersionException {
        this(version, basepathDir, true);
    }

    public JSONGameLauncher(GameVersion version, File basepathDir, boolean allowNativesArchFallback) throws LauncherVersionException {
        if (version.getMinimumLauncherVersion() > GameVersion.MINIMUM_LAUNCHER_VERSION_SUPPORTED) {
            throw new LauncherVersionException("Unsupported launcher version (" + version.getMinimumLauncherVersion() + ")");
        }

        this.allowNativesArchFallback = allowNativesArchFallback;
        versionInfo = version;
        this.basepathDir = basepathDir;
        this.librariesDir = new File(basepathDir, "libraries");
        this.versionsDir = new File(basepathDir, "versions");
        this.gameDir = basepathDir;
        this.assetsDir = new File(basepathDir, "assets");
        this.assetsIndexesDir = new File(assetsDir, "indexes");
        this.assetsObjectsDir = new File(assetsDir, "objects");
        this.virtualAssetsDir = new File(assetsDir, "virtual");

        initialize();
    }

    private static File findJavaExecutable() {
        return new File(System.getProperty("java.home"), "bin/java");
    }

    private void initialize() {
        gameVirtualAssetsDir = new File(virtualAssetsDir, versionInfo.getAssets());

        variables.put("version_name", versionInfo.getID());
        variables.put("version_type", versionInfo.getReleaseType().toString());
        variables.put("game_directory", basepathDir.getAbsolutePath());
        variables.put("user_properties", "{}");
        variables.put("user_type", "legacy");
        variables.put("auth_uuid", new UUID(0, 0).toString());
        variables.put("auth_access_token", "12345");
        variables.put("assets_index_name", versionInfo.getAssets());
        variables.put("assets_root", assetsDir.getAbsolutePath());
        variables.put("game_assets", gameVirtualAssetsDir.getAbsolutePath());
        variables.put("launcher_name", LauncherConstant.NAME);
        variables.put("launcher_version", LauncherConstant.VERSION);
    }

    private File getDependencyFile(DependencyName name) {
        return getDependencyFile(name, null);
    }

    private File getDependencyFile(DependencyName name, String classifier) {
        return name.getFile(librariesDir, classifier);
    }

    private void extractNatives(File nativesDir, StartupConfiguration config) throws IOException {
        ZipInputStream zip;
        OutputStream out;
        ZipEntry entry;
        byte[] buffer = new byte[4096];
        int read;

        for (GameLibrary library : versionInfo.getLibraries()) {
            if (library.isNativesLibrary() && library.isAllowed(config)) {
                File nativesFile = getDependencyFile(library.getDependencyName(),
                        library.getNatives().getNative());

                if (allowNativesArchFallback && !nativesFile.isFile() && config.getArchitecture() != OperatingSystem.Architecture.ARCH_32) {
                    nativesFile = getDependencyFile(library.getDependencyName(),
                            library.getNatives().getNative(config.getOperatingSystem(), OperatingSystem.Architecture.ARCH_32));
                }

                ExtractConfiguration extractConf = library.getExtractConfiguration();
                List<String> excludeList = extractConf == null ? null : extractConf.getExcludeList();

                zip = new ZipInputStream(new FileInputStream(nativesFile));

                librariesLoop:
                for (; (entry = zip.getNextEntry()) != null; zip.closeEntry()) {
                    if (excludeList != null) {
                        for (String exclude : excludeList) {
                            if (entry.getName().startsWith(exclude)) {
                                continue librariesLoop;
                            }
                        }
                    }

                    File file = new File(nativesDir, entry.getName());

                    if (entry.isDirectory()) {
                        if (!file.isDirectory() && !file.mkdirs()) {
                            throw new IOException("Can't create directory: " + file.getAbsolutePath());
                        }

                        continue;
                    }

                    File parentFile = file.getParentFile();
                    if (!parentFile.exists() && !parentFile.mkdirs()) {
                        throw new IOException("Can't create directory: " + parentFile.getAbsolutePath());
                    }

                    out = new FileOutputStream(file);

                    while ((read = zip.read(buffer)) != -1) {
                        out.write(buffer, 0, read);
                    }

                    out.close();
                }

                zip.close();
            }
        }
    }

    private void installVirtualAssets() throws IOException {
        File assetsFile = new File(assetsIndexesDir, versionInfo.getAssets() + ".json");

        if (!assetsFile.isFile()) {
            return;
        }

        Assets assets = new Gson().fromJson(new FileReader(assetsFile), Assets.class);

        new VirtualAssetsInstaller(assets, assetsObjectsDir, gameVirtualAssetsDir).install();
    }

    private File getLibraryClasspath(GameLibrary original) throws Exception {
        DependencyName depName = original.getDependencyName();
        File libFile;

        if (original.getDownloadInfo() == null ||
                original.getDownloadInfo().getArtifactInfo() == null ||
                original.getDownloadInfo().getArtifactInfo().getPath() == null) {
            libFile = getDependencyFile(depName);
        } else {
            libFile = new File(librariesDir, original.getDownloadInfo().getArtifactInfo().getPath());
        }

        if (libFile != null && !libFile.isFile()) {
            throw new FileNotFoundException("Library \"" + libFile.getAbsolutePath() + "\" is missing!");
        }

        return libFile;
    }

    private String buildClasspath(StartupConfiguration config) throws Exception {
        StringBuilder sb = new StringBuilder();

        File versionJar = versionInfo.getJarFile(basepathDir);

        if (!versionJar.isFile()) {
            throw new FileNotFoundException("Game JAR \"" + versionJar.getAbsolutePath() + "\" is missing");
        }

        sb.append(versionJar.getAbsolutePath());

        for (GameLibrary library : versionInfo.getLibraries()) {
            if (!library.isJavaLibrary() || !library.isAllowed(config)) {
                continue;
            }

            sb.append(File.pathSeparatorChar);
            sb.append(getLibraryClasspath(library).getAbsolutePath());
        }

        return sb.toString();
    }

    private String substituteVariables(String s, Map<String, String> map) {
        StringBuilder sb = new StringBuilder();

        int prev = 0;
        int start = prev;
        while ((start = s.indexOf("${", start)) != -1) {
            sb.append(s, prev, start);

            int tail = s.indexOf("}", start);
            if (tail == -1) {
                break;
            }

            String key = s.substring(start + 2, tail);
            if (map.containsKey(key)) {
                sb.append(map.get(key));
            } else {
                sb.append(s, start, tail + 1);
            }

            start = tail + 1;
            prev = start;
        }

        sb.append(s.substring(prev));

        return sb.toString();
    }

    private List<String> substituteVariables(Iterable<String> iter, Map<String, String> map) {
        List<String> result = new ArrayList<>();
        for (String s : iter) {
            result.add(substituteVariables(s, map));
        }
        return result;
    }

    private List<String> substituteVariables(Iterable<String> iter, StartupConfiguration config) {
        return substituteVariables(iter, config.getVariables());
    }

    private List<String> buildJVMArgs(File nativesDir, String classpath, StartupConfiguration config) throws Exception {
        List<String> result = new ArrayList<>();

        List<String> fromManifest = versionInfo.getJVMArgumentsOnConfig(config);
        if (fromManifest == null) {
            result.add("-cp");
            result.add(classpath);
            result.add("-Djava.library.path=" + nativesDir.getAbsolutePath());
        } else {
            result.addAll(fromManifest);
        }

        result.addAll(customJvmArgs);
        result.add(versionInfo.getMainClass());

        return substituteVariables(result, config);
    }

    private List<String> buildGameArgs(StartupConfiguration config) {
        List<String> result = new ArrayList<>();
        List<String> argsManifest = versionInfo.getGameArgumentsOnConfig(config);

        if (argsManifest != null) {
            result.addAll(argsManifest);
        }

        result.addAll(customGameArgs);

        return substituteVariables(result, config);
    }

    private List<String> buildProcessArgs(File nativesDir, String classpath, StartupConfiguration config) throws Exception {
        ArrayList<String> list = new ArrayList<>();
        list.add(findJavaExecutable().getAbsolutePath());
        list.addAll(buildJVMArgs(nativesDir, classpath, config));
        list.addAll(buildGameArgs(config));
        return list;
    }

    private ProcessBuilder buildProcess(File nativesDir, String classpath, StartupConfiguration config) throws Exception {
        ProcessBuilder processBuilder = new ProcessBuilder(buildProcessArgs(nativesDir, classpath, config));
        processBuilder.directory(gameDir);
        return processBuilder;
    }

    public void setUserProperties(UserProperties properties) {
        variables.put("user_properties", new Gson().toJson(properties.getProperties()));
    }

    @Override
    public Process launch() throws Exception {
        long time = System.nanoTime();

        File versionDir = new File(versionsDir, versionInfo.getID());
        final File nativesDir = new File(versionDir, versionInfo.getID() + "-natives-" + time);

        StartupConfiguration config = StartupConfiguration.getRunningConfig(features);

        String classpath = buildClasspath(config);

        Map<String, String> variablesEx = new HashMap<>(variables);
        variablesEx.put("natives_directory", nativesDir.getAbsolutePath());
        variablesEx.put("classpath", classpath);

        config = config.withVariables(variablesEx);

        extractNatives(nativesDir, config);

        if (versionInfo.getGameArgumentsOnConfig(config).contains("${game_assets}")) {
            installVirtualAssets();
        }

        ProcessBuilder pb = buildProcess(nativesDir, classpath, config);
        final Process process = pb.start();

        final Thread removeFilesThread = new Thread("Remove natives and patched libraries") {

            @Override
            public void run() {
                try {
                    process.waitFor();
                } catch (InterruptedException e) {
                    LauncherConstant.LOGGER.catching(e);
                }

                FileUtil.deleteFileRescursive(nativesDir);
            }
        };

        removeFilesThread.start();

        Runtime.getRuntime().addShutdownHook(new Thread("Finish remove unused files thread") {

            @Override
            public void run() {
                try {
                    removeFilesThread.join(15000);
                } catch (InterruptedException e) {
                    LauncherConstant.LOGGER.catching(e);
                }
            }
        });

        return process;
    }

    @Override
    public File getGameDirectory() {
        return gameDir;
    }

    @Override
    public void setGameDirectory(File dir) {
        gameDir = dir;
    }

    @Override
    public void setVariable(String key, String value) {
        variables.put(key, value);
    }

    @Override
    public void addJVMArgument(String arg) {
        customJvmArgs.add(arg);
    }

    @Override
    public void addGameArgument(String arg) {
        customGameArgs.add(arg);
    }

    @Override
    public void setUserInformation(UserInformation profile) {
        setVariable("auth_player_name", profile.getUsername());
        setVariable("auth_uuid", profile.getID());
        setVariable("auth_access_token", profile.getAccessToken());
    }

    @Override
    public void setFeature(String key, boolean value) {
        features.put(key, value);
    }

    @Override
    public Boolean getFeature(String key) {
        return features.get(key);
    }

    @Override
    public void removeFeature(String key) {
        features.remove(key);
    }
}
