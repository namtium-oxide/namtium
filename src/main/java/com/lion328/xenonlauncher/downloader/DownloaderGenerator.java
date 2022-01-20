// Copyright (C) 2016-2022 Waritnan Sookbuntherng
// SPDX-License-Identifier: Apache-2.0

package com.lion328.xenonlauncher.downloader;

import java.io.IOException;
import java.util.List;

public interface DownloaderGenerator
{

    List<Downloader> generateDownloaders() throws IOException;
}
