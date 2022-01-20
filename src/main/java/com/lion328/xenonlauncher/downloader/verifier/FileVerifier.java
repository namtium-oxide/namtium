// Copyright (C) 2016-2022 Waritnan Sookbuntherng
// SPDX-License-Identifier: Apache-2.0

package com.lion328.xenonlauncher.downloader.verifier;

import java.io.File;
import java.io.IOException;

public interface FileVerifier
{

    boolean isValid(File file) throws IOException;
}
