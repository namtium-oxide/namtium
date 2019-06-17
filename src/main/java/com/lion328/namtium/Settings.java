package com.lion328.namtium;

import com.lion328.hydra.HydraSettings;
import com.lion328.xenonlauncher.util.URLUtil;

import java.net.MalformedURLException;
import java.net.URL;

public class Settings implements HydraSettings
{

    private static Settings instance;

    private static final URL launcherBaseURL = URLUtil.constantURL("http://example.com/launcher/");
    private static final URL registerURL = URLUtil.constantURL("http://example.com/register");
    private static final URL authURL = fromBase("api/authenticate.php");
    private static final URL whitelistURL = fromBase("whitelist");
    private static final URL compressedFilesURL = fromBase("filelist.gz");
    private static final URL filesURL = fromBase("files/");

    private Settings()
    {

    }

    @Override
    public URL getRegisterURL()
    {
        return registerURL;
    }

    @Override
    public URL getAuthenticationURL()
    {
        return authURL;
    }

    @Override
    public URL getWhitelistFileListURL()
    {
        return whitelistURL;
    }

    @Override
    public URL getCompressedFileListURL()
    {
        return compressedFilesURL;
    }

    @Override
    public URL getFileBaseURL()
    {
        return filesURL;
    }

    @Override
    public String getApplicationDirectoryName()
    {
        return "namtium";
    }

    @Override
    public String getGameVersionName()
    {
        return "game";
    }

    @Override
    public String[] getVMArguments()
    {
        return new String[] {
                "-XX:HeapDumpPath=MojangTricksIntelDriversForPerformance_javaw.exe_minecraft.exe.heapdump",
                "-XX:+UseConcMarkSweepGC",
                "-XX:+CMSIncrementalMode",
                "-XX:-UseAdaptiveSizePolicy"
        };
    }

    @Override
    public boolean streamGameOutput() {
        return true;
    }

    @Override
    public boolean passPasswordToGame() {
        return true;
    }

    private static URL fromBase(String s)
    {
        return URLUtil.constantURL(launcherBaseURL.toString() + s);
    }

    public static Settings getInstance()
    {
        if (instance == null)
        {
            instance = new Settings();
        }

        return instance;
    }
}
