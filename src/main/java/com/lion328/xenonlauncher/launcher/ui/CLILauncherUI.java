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

package com.lion328.xenonlauncher.launcher.ui;

import com.lion328.xenonlauncher.launcher.Launcher;

import java.io.File;
import java.util.Arrays;

public class CLILauncherUI implements LauncherUI
{

    private Launcher launcher;

    @Override
    public void start()
    {
        while (true)
        {
            String username = System.console().readLine("Username: ");
            char[] password = System.console().readPassword("Password: ");

            boolean state = launcher.loginAndLaunch(username, password);

            Arrays.fill(password, '\0');

            if (state)
            {
                return;
            }
        }
    }

    @Override
    public Launcher getLauncher()
    {
        return launcher;
    }

    @Override
    public void setLauncher(Launcher launcher)
    {
        this.launcher = launcher;
    }

    @Override
    public boolean isVisible()
    {
        return true;
    }

    @Override
    public void setVisible(boolean visible)
    {
        // nothing doing here
    }

    @Override
    public void displayError(String message)
    {
        System.err.println(message);
    }

    @Override
    public void onPercentageChange(File file, int overallPercentage, long fileSize, long fileDownloaded)
    {
        StringBuilder line = new StringBuilder();
        line.append('[');

        int halfProgress = overallPercentage / 2;

        for (int i = 0; i < 50; i++)
        {
            if (halfProgress >= i)
            {
                line.append('#');
            }
            else
            {
                line.append(' ');
            }
        }

        line.append("] ");
        line.append(overallPercentage);
        line.append("% ");
        line.append(file.getName());
        line.append(' ');

        if (fileSize != -1 && fileDownloaded != -1)
        {
            line.append(fileDownloaded);
            line.append(fileSize);
        }

        for (int i = 0; i < 20; i++)
        {
            line.append(' ');
        }

        System.out.print('\r');
        System.out.print(line.toString());
    }
}
