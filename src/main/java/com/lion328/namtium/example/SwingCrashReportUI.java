// Copyright (C) 2017-2022 Waritnan Sookbuntherng
// SPDX-License-Identifier: Apache-2.0

package com.lion328.namtium.example;

import com.lion328.namtium.launcher.hydra.CrashReportUI;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class SwingCrashReportUI implements CrashReportUI {

    private JFrame frame;
    private JTextArea reportText;

    public SwingCrashReportUI() {
        reportText = new JTextArea();
        reportText.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        reportText.setLineWrap(true);
        reportText.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(reportText);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(800, 450));
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        scrollPane.setBackground(new Color(0x330044));

        frame = new JFrame();
        frame.add(scrollPane);
        frame.pack();
        frame.setLocationRelativeTo(null);
    }

    @Override
    public void onGameCrash(final File crashReportFile) {
        String report;

        try {
            report = new String(Files.readAllBytes(crashReportFile.toPath()), StandardCharsets.UTF_8);
        } catch (IOException e) {
            Main.getLogger().catching(e);
            report = "Can't read crash report file: " + crashReportFile.getAbsolutePath() + "\n" + e.getMessage();
        }

        final String finalReport = report;

        reportText.setText(finalReport);
        reportText.setCaretPosition(0);

        frame.setTitle("Minecraft Crash Report (" + crashReportFile.getName() + ")");
        frame.setVisible(true);

        // TODO: remove this hack
        while (frame.isVisible()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {}
        }
    }
}
