// Copyright (C) 2018-2022 Waritnan Sookbuntherng
// SPDX-License-Identifier: Apache-2.0

package com.lion328.namtium;

import com.google.gson.Gson;
import com.lion328.hydra.HydraLauncher;
import com.lion328.hydra.HydraLauncherUI;
import com.lion328.hydra.Language;
import com.lion328.hydra.PlayerSettingsUI;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.FontUIResource;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class Main {

    public static final String VERSION;
    public static final String LANGUAGE_NAMESPACE = "namtium";
    public static final String LANGUAGE_PREFIX = LANGUAGE_NAMESPACE + ".";

    private static Logger logger;
    private static String[] fontsName;
    private static Font[] fonts;

    public static void main(String[] args) {
        if (args.length >= 1 && args[0].equals("--version")) {
            System.out.println(VERSION);

            return;
        }

        getLogger().info("Namtium launcher version " + VERSION);
        getLogger().info("Register language...");
        registerLanguage();

        getLogger().info("Enable anti-aliasing for swing...");
        System.setProperty("swing.aatext", "true");
        System.setProperty("awt.useSystemAAFontSettings", "on");

        try {
            registerFonts();

            if (!setLookAndFeel("Metal")) {
                getLogger().info("Failed to set Metal as swing look and feel");
            }
        } catch (Exception e) {
            getLogger().catching(e);
        }

        if (fonts.length != 0) {
            setGlobalSwingFont(new FontUIResource(fonts[0].deriveFont(Font.PLAIN, 14)));
        }

        getLogger().info("Changing ProgressBar color...");
        UIManager.put("ProgressBar.selectionBackground", Color.GRAY);
        UIManager.put("ProgressBar.foreground", new Color(0x0EB600));

        getLogger().info("Opening launcher...");

        HydraLauncherUI launcherUI = new DefaultLauncherUI();
        PlayerSettingsUI playerSettingsUI = new SettingsUI(launcherUI.getJFrame());

        HydraLauncher launcher = new HydraLauncher(Settings.getInstance());
        launcher.setLauncherUI(launcherUI);
        launcher.setSettingsUI(playerSettingsUI);
        launcher.start();
    }

    public static Logger getLogger() {
        if (logger == null) {
            logger = LogManager.getLogger("Namtium-Launcher");
        }

        return logger;
    }

    public static String lang(String key) {
        return Language.get(LANGUAGE_PREFIX + key);
    }

    private static void registerFonts() throws IOException, FontFormatException {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String fontName;
        Font font;

        for (int i = 0; i < fontsName.length; i++) {
            fontName = fontsName[i];

            getLogger().info("Registering font " + fontName);

            fontName = "resources/fonts/" + fontName + ".ttf";
            font = Font.createFont(Font.TRUETYPE_FONT, Main.class.getResourceAsStream(fontName));

            fonts[i] = font;
            ge.registerFont(font);
        }
    }

    private static void setGlobalSwingFont(FontUIResource font) {
        getLogger().info("Setting " + font.getName() + " as default font for swing");

        Enumeration<Object> keys = UIManager.getDefaults().keys();

        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);

            if (value instanceof javax.swing.plaf.FontUIResource) {
                UIManager.put(key, font);
            }
        }
    }

    private static boolean setLookAndFeel(String nameSnippet) throws ClassNotFoundException,
            UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        UIManager.LookAndFeelInfo[] plafs = UIManager.getInstalledLookAndFeels();

        for (UIManager.LookAndFeelInfo info : plafs) {
            if (info.getName().contains(nameSnippet)) {
                getLogger().info("Set swing look and feel to " + info.getClassName());

                UIManager.setLookAndFeel(info.getClassName());

                return true;
            }
        }

        return false;
    }

    private static void registerLanguage() {
        Map table = new Gson()
                .fromJson(
                        new InputStreamReader(
                                Language.class.getResourceAsStream("/com/lion328/namtium/resources/lang.json"),
                                StandardCharsets.UTF_8
                        ),
                        Map.class
                );

        if (table != null) {
            Map ret = new HashMap();

            for (Object key : table.keySet()) {
                ret.put(LANGUAGE_PREFIX + key, table.get(key));
            }

            Language.extend(ret);
        }
    }

    static {
        String version = "UNKNOWN";

        try {
            version = new BufferedReader(
                    new InputStreamReader(
                            Main.class.getResourceAsStream("/com/lion328/namtium/resources/version")
                    )
            ).readLine().trim();
        } catch (IOException e) {
            getLogger().catching(e);
        }

        VERSION = version;

        fontsName = new String[]{
                "CSChatThaiUI"
        };

        fonts = new Font[fontsName.length];
    }
}
