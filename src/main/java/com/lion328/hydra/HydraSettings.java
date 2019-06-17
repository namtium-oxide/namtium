package com.lion328.hydra;

import java.net.URL;

public interface HydraSettings
{

    URL getRegisterURL();

    URL getAuthenticationURL();

    URL getWhitelistFileListURL();

    URL getCompressedFileListURL();

    URL getFileBaseURL();

    String getApplicationDirectoryName();

    String getGameVersionName();

    String[] getVMArguments();

    boolean streamGameOutput();

    boolean passPasswordToGame();
}
