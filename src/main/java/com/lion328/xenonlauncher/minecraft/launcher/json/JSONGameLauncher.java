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

package com.lion328.xenonlauncher.minecraft.launcher.json;

import com.google.gson.Gson;
import com.lion328.xenonlauncher.downloader.repository.DependencyName;
import com.lion328.xenonlauncher.minecraft.api.authentication.UserInformation;
import com.lion328.xenonlauncher.minecraft.api.authentication.yggdrasil.UserProperties;
import com.lion328.xenonlauncher.minecraft.assets.VirtualAssetsInstaller;
import com.lion328.xenonlauncher.minecraft.assets.data.Assets;
import com.lion328.xenonlauncher.minecraft.launcher.BasicGameLauncher;
import com.lion328.xenonlauncher.minecraft.launcher.json.data.GameLibrary;
import com.lion328.xenonlauncher.minecraft.launcher.json.data.GameVersion;
import com.lion328.xenonlauncher.minecraft.launcher.json.exception.LauncherVersionException;
import com.lion328.xenonlauncher.patcher.FilePatcher;
import com.lion328.xenonlauncher.settings.LauncherConstant;
import com.lion328.xenonlauncher.util.FileUtil;
import com.lion328.xenonlauncher.util.OS;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class JSONGameLauncher extends BasicGameLauncher
{

    private List<String> jvmArgs = new ArrayList<>();
    private List<String> gameArgs = new ArrayList<>();
    private Map<String, String> replaceArgs = new HashMap<>();
    private Map<DependencyName, FilePatcher> patchers = new HashMap<>();

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

    public JSONGameLauncher(GameVersion version, File basepathDir) throws LauncherVersionException
    {
        this(version, basepathDir, true);
    }

    public JSONGameLauncher(GameVersion version, File basepathDir, boolean allowNativesArchFallback) throws LauncherVersionException
    {
        if (version.getMinimumLauncherVersion() > GameVersion.PARSER_VERSION)
        {
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

    private static File findJavaExecutable()
    {
        return new File(System.getProperty("java.home"), "bin/java");
    }

    private void initialize()
    {
        gameVirtualAssetsDir = new File(virtualAssetsDir, versionInfo.getAssets());

        replaceArgs.put("version_name", versionInfo.getID());
        replaceArgs.put("version_type", versionInfo.getReleaseType().toString());
        replaceArgs.put("game_directory", basepathDir.getAbsolutePath());
        replaceArgs.put("user_properties", "{}");
        replaceArgs.put("user_type", "legacy");
        replaceArgs.put("auth_uuid", new UUID(0, 0).toString());
        replaceArgs.put("auth_access_token", "12345");
        replaceArgs.put("assets_index_name", versionInfo.getAssets());
        replaceArgs.put("assets_root", assetsDir.getAbsolutePath());
        replaceArgs.put("game_assets", gameVirtualAssetsDir.getAbsolutePath());
    }

    private File getDependencyFile(DependencyName name)
    {
        return getDependencyFile(name, null);
    }

    private File getDependencyFile(DependencyName name, String classifier)
    {
        return name.getFile(librariesDir, classifier);
    }

    private boolean isMatchRegex(DependencyName depName, DependencyName regexDepName)
    {
        return depName.equals(regexDepName);
    }

    private void extractNatives(File nativesDir) throws IOException
    {
        File nativesFile, parentFile;
        ZipInputStream zip;
        OutputStream out;
        ZipEntry entry;
        byte[] buffer = new byte[4096];
        int read;

        for (GameLibrary library : versionInfo.getLibraries())
        {
            if (library.isNativesLibrary() && library.isAllowed())
            {
                nativesFile = getDependencyFile(library.getDependencyName(),
                        library.getNatives().getNative());

                if (allowNativesArchFallback && !nativesFile.isFile() && OS.getCurrentArchitecture() != OS.Architecture.ARCH_32)
                {
                    nativesFile = getDependencyFile(library.getDependencyName(),
                            library.getNatives().getNative(OS.getCurrentOS(), OS.Architecture.ARCH_32));
                }

                zip = new ZipInputStream(new FileInputStream(nativesFile));

                librariesLoop:
                for (; (entry = zip.getNextEntry()) != null; zip.closeEntry())
                {
                    for (String exclude : library.getExtractRule().getExcludeList())
                    {
                        if (entry.getName().startsWith(exclude))
                        {
                            continue librariesLoop;
                        }
                    }

                    if (!entry.isDirectory())
                    {
                        parentFile = new File(nativesDir, entry.getName()).getParentFile();
                        if (!parentFile.exists() && !parentFile.mkdirs())
                        {
                            throw new IOException("Can't create directory");
                        }
                    }

                    out = new FileOutputStream(new File(nativesDir, entry.getName()));

                    while ((read = zip.read(buffer)) != -1)
                    {
                        out.write(buffer, 0, read);
                    }

                    out.close();
                }

                zip.close();
            }
        }
    }

    private void installVirtualAssets() throws IOException
    {
        File assetsFile = new File(assetsIndexesDir, versionInfo.getAssets() + ".json");

        if (!assetsFile.isFile())
        {
            return;
        }

        Assets assets = new Gson().fromJson(new FileReader(assetsFile), Assets.class);

        new VirtualAssetsInstaller(assets, assetsObjectsDir, gameVirtualAssetsDir).install();
    }

    private File patchLibrary(GameLibrary original, File dir) throws Exception
    {
        DependencyName depName = original.getDependencyName();
        File libFile;

        if (original.getDownloadInfo() == null ||
                original.getDownloadInfo().getArtifactInfo() == null ||
                original.getDownloadInfo().getArtifactInfo().getPath() == null)
        {
            libFile = getDependencyFile(depName);
        }
        else
        {
            libFile = new File(librariesDir, original.getDownloadInfo().getArtifactInfo().getPath());
        }

        if (libFile != null && !libFile.isFile())
        {
            throw new FileNotFoundException("Library \"" + libFile.getAbsolutePath() + "\" is missing!");
        }

        return patchLibrary(depName, libFile, new File(dir, depName.getShortName().replace(':', '-') + ".jar"));
    }

    private File patchLibrary(DependencyName depName, File inFile, File outFile) throws Exception
    {
        DependencyName regexDepName;

        // check first
        boolean flag = true;
        for (Map.Entry<DependencyName, FilePatcher> entry : patchers.entrySet())
        {
            regexDepName = entry.getKey();
            if (isMatchRegex(depName, regexDepName))
            {
                flag = false;
                break;
            }
        }

        if (flag)
        {
            return inFile;
        }

        if (!outFile.getParentFile().exists() && !outFile.getParentFile().mkdirs())
        {
            throw new IOException("Can't create directory");
        }

        JarOutputStream zipOut = new JarOutputStream(new FileOutputStream(outFile));
        ZipInputStream zipIn = new ZipInputStream(new FileInputStream(inFile));
        ZipEntry zipEntry;

        ByteArrayOutputStream byteTmp = new ByteArrayOutputStream(8192);
        byte[] buffer = new byte[8192];
        int read;

        for (; (zipEntry = zipIn.getNextEntry()) != null; zipIn.closeEntry())
        {
            if (zipEntry.getName().startsWith("META-INF/"))
            {
                continue;
            }

            zipEntry = new ZipEntry(zipEntry.getName());
            zipOut.putNextEntry(zipEntry);

            if (!zipEntry.isDirectory())
            {
                byteTmp.reset();

                while ((read = zipIn.read(buffer)) != -1)
                {
                    byteTmp.write(buffer, 0, read);
                }

                byte[] byteOut = byteTmp.toByteArray();

                for (Map.Entry<DependencyName, FilePatcher> entry : patchers.entrySet())
                {
                    regexDepName = entry.getKey();

                    if (!isMatchRegex(depName, regexDepName))
                    {
                        continue;
                    }

                    byteOut = entry.getValue().patchFile(zipEntry.getName(), byteOut);
                }

                zipOut.write(byteOut);
            }

            zipOut.closeEntry();
        }

        zipIn.close();
        zipOut.close();

        return outFile;
    }

    private String buildClasspath(File patchedLibDir) throws Exception
    {
        StringBuilder sb = new StringBuilder();

        File versionJar = versionInfo.getJarFile(basepathDir);

        if (!versionJar.isFile())
        {
            throw new FileNotFoundException("Game JAR \"" + versionJar.getAbsolutePath() + "\" is missing");
        }

        sb.append(patchLibrary(new DependencyName("net.minecraft:client:" + versionInfo.getID()), versionJar, new File(patchedLibDir, versionInfo.getID() + ".jar")).getAbsolutePath());

        for (GameLibrary library : versionInfo.getLibraries())
        {
            if (!library.isJavaLibrary() || !library.isAllowed())
            {
                continue;
            }

            sb.append(File.pathSeparatorChar);
            sb.append(patchLibrary(library, patchedLibDir).getAbsolutePath());
        }

        return sb.toString();
    }

    private List<String> buildJVMArgs(File nativesDir, File patchedLibDir) throws Exception
    {
        List<String> list = new ArrayList<>();

        list.add("-cp");
        list.add(buildClasspath(patchedLibDir));

        list.add("-Djava.library.path=" + nativesDir.getAbsolutePath());

        list.addAll(jvmArgs);

        list.add(versionInfo.getMainClass());

        return list;
    }

    private List<String> buildGameArgs()
    {
        String[] mcArgs = versionInfo.getMinecraftArguments().split(" ");
        String value;
        String mcArg;

        for (int i = 0; i < mcArgs.length; i++)
        {
            mcArg = mcArgs[i];

            if (!mcArg.startsWith("${") || !mcArg.endsWith("}"))
            {
                continue;
            }

            value = replaceArgs.get(mcArg.substring(2, mcArg.length() - 1));

            if (value == null)
            {
                continue;
            }

            mcArgs[i] = value;
        }

        List<String> mcArgsList = new ArrayList<>();
        mcArgsList.addAll(Arrays.asList(mcArgs));
        mcArgsList.addAll(gameArgs);

        return mcArgsList;
    }

    private List<String> buildProcessArgs(File nativesDir, File patchedLibDir) throws Exception
    {
        ArrayList<String> list = new ArrayList<>();
        list.add(findJavaExecutable().getAbsolutePath());
        list.addAll(buildJVMArgs(nativesDir, patchedLibDir));
        list.addAll(buildGameArgs());
        return list;
    }

    private ProcessBuilder buildProcess(File nativesDir, File patchedLibDir) throws Exception
    {
        ProcessBuilder processBuilder = new ProcessBuilder(buildProcessArgs(nativesDir, patchedLibDir));
        processBuilder.directory(gameDir);
        return processBuilder;
    }

    public void setUserProperties(UserProperties properties)
    {
        replaceArgs.put("user_properties", new Gson().toJson(properties.getProperties()));
    }

    @Override
    public Process launch() throws Exception
    {
        long time = System.nanoTime();

        File versionDir = new File(versionsDir, versionInfo.getID());
        final File nativesDir = new File(versionDir, versionInfo.getID() + "-natives-" + time);
        final File tmpLibraryDir = new File(versionDir, versionInfo.getID() + "-patchedlib-" + time);

        extractNatives(nativesDir);

        if (versionInfo.getMinecraftArguments().contains("${game_assets}"))
        {
            installVirtualAssets();
        }

        ProcessBuilder pb = buildProcess(nativesDir, tmpLibraryDir);
        final Process process = pb.start();

        final Thread removeFilesThread = new Thread("Remove natives and patched libraries")
        {

            @Override
            public void run()
            {
                try
                {
                    process.waitFor();
                }
                catch (InterruptedException e)
                {
                    LauncherConstant.LOGGER.catching(e);
                }

                FileUtil.deleteFileRescursive(nativesDir);
                FileUtil.deleteFileRescursive(tmpLibraryDir);
            }
        };

        removeFilesThread.start();

        Runtime.getRuntime().addShutdownHook(new Thread("Finish remove unused files thread")
        {

            @Override
            public void run()
            {
                try
                {
                    removeFilesThread.join(15000);
                }
                catch (InterruptedException e)
                {
                    LauncherConstant.LOGGER.catching(e);
                }
            }
        });

        return process;
    }

    @Override
    public File getGameDirectory()
    {
        return gameDir;
    }

    @Override
    public void setGameDirectory(File dir)
    {
        gameDir = dir;
    }

    @Override
    public void addPatcher(DependencyName regex, FilePatcher patcher)
    {
        patchers.put(regex, patcher);
    }

    @Override
    public void replaceArgument(String key, String value)
    {
        replaceArgs.put(key, value);
    }

    @Override
    public void addJVMArgument(String arg)
    {
        jvmArgs.add(arg);
    }

    @Override
    public void addGameArgument(String arg)
    {
        gameArgs.add(arg);
    }

    @Override
    public void setUserInformation(UserInformation profile)
    {
        replaceArgument("auth_player_name", profile.getUsername());
        replaceArgument("auth_uuid", profile.getID());
        replaceArgument("auth_access_token", profile.getAccessToken());
    }
}
