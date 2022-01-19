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

package com.lion328.xenonlauncher.launcher;

import com.lion328.xenonlauncher.downloader.Downloader;
import com.lion328.xenonlauncher.launcher.ui.LauncherUI;
import com.lion328.xenonlauncher.minecraft.api.authentication.MinecraftAuthenticator;
import com.lion328.xenonlauncher.minecraft.api.authentication.UserInformation;
import com.lion328.xenonlauncher.minecraft.api.authentication.exception.MinecraftAuthenticatorException;
import com.lion328.xenonlauncher.minecraft.launcher.GameLauncher;
import com.lion328.xenonlauncher.util.io.ProcessOutput;

import java.io.IOException;

public class BasicLauncher implements Launcher
{

    private final MinecraftAuthenticator mcAuth;
    private final Downloader downloader;
    private final GameLauncher mcLauncher;

    private LauncherUI ui;
    private UserInformation userInfo;

    public BasicLauncher(MinecraftAuthenticator mcAuth, Downloader downloader, GameLauncher mcLauncher)
    {
        this.mcAuth = mcAuth;
        this.downloader = downloader;
        this.mcLauncher = mcLauncher;
    }

    public Downloader getDownloader()
    {
        return downloader;
    }

    public MinecraftAuthenticator getMinecraftAuthenticator()
    {
        return mcAuth;
    }

    public GameLauncher getMinecraftLauncher()
    {
        return mcLauncher;
    }

    @Override
    public void start()
    {
        ui.setVisible(true);
        ui.start();
    }

    @Override
    public UserInformation getCacheUser() throws IOException
    {
        return userInfo;
    }

    @Override
    public void setCacheUser(UserInformation userInfo) throws IOException
    {
        this.userInfo = userInfo;
    }

    @Override
    public void clearCacheUser() throws IOException
    {
        userInfo = null;
    }

    @Override
    public LauncherUI getLauncherUI()
    {
        return ui;
    }

    @Override
    public void setLauncherUI(LauncherUI ui)
    {
        this.ui = ui;

        ui.setLauncher(this);
    }

    @Override
    public boolean loginAndLaunch(String username, char[] password)
    {
        try
        {
            mcAuth.login(username, password);

            userInfo = mcAuth.getUserInformation();
        }
        catch (IOException | MinecraftAuthenticatorException e)
        {
            displayError(e.toString());

            return false;
        }

        downloader.registerCallback(ui);

        try
        {
            downloader.download();
            downloader.removeCallback(ui);
        }
        catch (IOException e)
        {
            displayError(e.toString());

            downloader.removeCallback(ui);

            return false;
        }

        mcLauncher.setUserInformation(userInfo);

        try
        {
            final Process process = mcLauncher.launch();

            /*new Thread()
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
                        displayError(e.toString());
                    }

                    exit();
                }
            }.start();*/

            new Thread()
            {

                @Override
                public void run()
                {
                    int b;
                    try
                    {
                        while ((b = process.getErrorStream().read()) != -1)
                        {
                            System.err.write(b);
                        }
                    }
                    catch (IOException e)
                    {
                        displayError(e.toString());
                    }
                }
            }.start();

            Thread td = new Thread()
            {

                @Override
                public void run()
                {
                    int b;
                    try
                    {
                        while ((b = process.getInputStream().read()) != -1)
                        {
                            System.out.write(b);
                        }
                    }
                    catch (IOException e)
                    {
                        displayError(e.toString());
                    }
                }
            };
            td.start();
        }
        catch (Exception e)
        {
            displayError(e.toString());

            return false;
        }

        ui.setVisible(false);

        return true;
    }

    @Override
    public void exit()
    {
        System.exit(0);
    }

    private void displayError(String message)
    {
        if (ui != null)
        {
            ui.displayError(message);
        }
    }
}
