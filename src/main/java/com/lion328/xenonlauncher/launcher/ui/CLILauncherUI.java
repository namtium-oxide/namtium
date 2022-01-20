// Copyright (C) 2016-2022 Waritnan Sookbuntherng
// SPDX-License-Identifier: Apache-2.0

package com.lion328.xenonlauncher.launcher.ui;

import com.lion328.xenonlauncher.launcher.Launcher;

import java.io.File;
import java.util.Arrays;

public class CLILauncherUI implements LauncherUI {

    private Launcher launcher;

    @Override
    public void start() {
        while (true) {
            String username = System.console().readLine("Username: ");
            char[] password = System.console().readPassword("Password: ");

            boolean state = launcher.loginAndLaunch(username, password);

            Arrays.fill(password, '\0');

            if (state) {
                return;
            }
        }
    }

    @Override
    public Launcher getLauncher() {
        return launcher;
    }

    @Override
    public void setLauncher(Launcher launcher) {
        this.launcher = launcher;
    }

    @Override
    public boolean isVisible() {
        return true;
    }

    @Override
    public void setVisible(boolean visible) {
        // nothing doing here
    }

    @Override
    public void displayError(String message) {
        System.err.println(message);
    }

    @Override
    public void onPercentageChange(File file, int overallPercentage, long fileSize, long fileDownloaded) {
        StringBuilder line = new StringBuilder();
        line.append('[');

        int halfProgress = overallPercentage / 2;

        for (int i = 0; i < 50; i++) {
            if (halfProgress >= i) {
                line.append('#');
            } else {
                line.append(' ');
            }
        }

        line.append("] ");
        line.append(overallPercentage);
        line.append("% ");
        line.append(file.getName());
        line.append(' ');

        if (fileSize != -1 && fileDownloaded != -1) {
            line.append(fileDownloaded);
            line.append(fileSize);
        }

        for (int i = 0; i < 20; i++) {
            line.append(' ');
        }

        System.out.print('\r');
        System.out.print(line.toString());
    }
}
