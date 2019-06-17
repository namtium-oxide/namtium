package com.lion328.namtium;

import com.lion328.hydra.HydraLauncher;
import com.lion328.hydra.HydraLauncherUI;
import com.lion328.hydra.ImagePanel;
import com.lion328.hydra.Language;
import com.lion328.hydra.Util;
import com.lion328.xenonlauncher.launcher.Launcher;
import com.lion328.xenonlauncher.util.URLUtil;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class DefaultLauncherUI implements HydraLauncherUI
{

    public static final URL NEWS_IMAGE_URL;
    public static final URL REGISTER_URL;

    private Launcher launcher;

    private JFrame frame;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JProgressBar statusProgressBar;
    private JLabel statusLabel;

    private boolean launching = false;
    private boolean updatingStatus = false;

    public DefaultLauncherUI()
    {
        frame = new JFrame();

        initializeFrame();
    }

    private void initializeFrame()
    {
        frame.addWindowListener(new WindowAdapter()
        {

            @Override
            public void windowClosing(WindowEvent e)
            {
                Main.getLogger().info("Closing launcher...");

                launcher.exit();
            }
        });

        final ImagePanel panel = new ImagePanel();

        try
        {
            panel.setImage(ImageIO.read(this.getClass().getResourceAsStream("/com/lion328/namtium/resources/bg.png")));
        }
        catch (IOException e)
        {
            Main.getLogger().catching(e);
        }

        panel.setLayout(null);
        panel.setPreferredSize(new Dimension(800, 500));

        frame.setLayout(new BorderLayout());
        frame.add(panel, BorderLayout.CENTER);
        frame.setResizable(false);
        frame.setTitle(Main.lang("title"));

        try
        {
            frame.setIconImage(
                    ImageIO.read(this.getClass().getResourceAsStream("/com/lion328/namtium/resources/favicon.png")));
        }
        catch (IOException e)
        {
            Main.getLogger().catching(e);
        }

        usernameField = new JTextField();
        passwordField = new JPasswordField();
        statusLabel = new JLabel();
        statusProgressBar = new JProgressBar();

        usernameField.setBounds(599, 70, 180, 32);
        passwordField.setBounds(599, 135, 180, 32);
        statusLabel.setBounds(75, 431, 700, 20);
        statusProgressBar.setBounds(15, 460, 545, 27);

        Border border = BorderFactory.createEmptyBorder(0, 5, 0, 5);
        usernameField.setBorder(border);
        passwordField.setBorder(border);

        KeyListener keyListener = new KeyAdapter()
        {

            @Override
            public void keyPressed(KeyEvent e)
            {
                if (!launching && e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    launch();
                }
            }
        };

        usernameField.addKeyListener(keyListener);
        passwordField.addKeyListener(keyListener);

        statusLabel.setForeground(Color.WHITE);
        statusProgressBar.setStringPainted(true);

        if (launcher instanceof HydraLauncher)
        {
            usernameField.setText(((HydraLauncher) launcher).getPlayerSettings().getPlayerName());
        }

        panel.add(usernameField);
        panel.add(passwordField);
        panel.add(statusLabel);
        panel.add(statusProgressBar);

        JPanel launchButton = new JPanel();
        JPanel registerButton = new JPanel();
        JPanel settingsButton = new JPanel();

        launchButton.setBounds(599, 184, 88, 88);
        launchButton.setOpaque(false);
        launchButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        registerButton.setBounds(692, 230, 92, 42);
        registerButton.setOpaque(false);
        registerButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        settingsButton.setBounds(692, 184, 92, 42);
        settingsButton.setOpaque(false);
        settingsButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        launchButton.addMouseListener(new MouseAdapter()
        {

            @Override
            public void mouseClicked(MouseEvent mouseEvent)
            {
                if (!launching)
                {
                    launch();
                }
            }
        });

        if (REGISTER_URL != null)
        {
            registerButton.addMouseListener(new MouseAdapter()
            {

                @Override
                public void mouseClicked(MouseEvent mouseEvent)
                {
                    if (!launching && Desktop.isDesktopSupported())
                    {
                        Util.openURL(REGISTER_URL);
                    }
                }
            });
        }

        settingsButton.addMouseListener(new MouseAdapter()
        {

            @Override
            public void mouseClicked(MouseEvent e)
            {
                if (!launching && launcher instanceof HydraLauncher)
                {
                    ((HydraLauncher) launcher).openSettingsDialog();
                }
            }
        });

        panel.add(launchButton);
        panel.add(registerButton);
        panel.add(settingsButton);

        JLabel versionLabel = new JLabel(Main.VERSION, SwingConstants.RIGHT);
        versionLabel.setBounds(600, 475, 190, 20);
        panel.add(versionLabel);

        if (NEWS_IMAGE_URL != null)
        {
            SwingUtilities.invokeLater(new Runnable()
            {

                @Override
                public void run()
                {
                    try
                    {
                        JPanel newsPanel = new ImagePanel(ImageIO.read(NEWS_IMAGE_URL));
                        newsPanel.setBounds(28, 19, 525, 375);
                        panel.add(newsPanel);
                    }
                    catch (IOException e)
                    {
                        Main.getLogger().catching(e);
                    }
                }
            });
        }

        resetUI();

        frame.pack();
        frame.setLocationRelativeTo(null);
    }

    private void launch()
    {
        new Thread()
        {

            @Override
            public void run()
            {
                disableTextfields();

                launching = true;

                if (launcher.loginAndLaunch(usernameField.getText(), passwordField.getPassword()))
                {
                    setVisible(false);
                    launcher.exit();
                }

                resetUI();
            }
        }.start();
    }

    private void disableTextfields()
    {
        usernameField.setEnabled(false);
        passwordField.setEnabled(false);
    }

    private void resetUI()
    {
        usernameField.setEnabled(true);
        passwordField.setEnabled(true);

        statusProgressBar.setString("0%");
        statusProgressBar.setValue(0);
        statusLabel.setText(Main.lang("idle"));

        launching = false;
    }

    @Override
    public JFrame getJFrame()
    {
        return frame;
    }

    @Override
    public void start()
    {
        if (launcher instanceof HydraLauncher)
        {
            usernameField.setText(((HydraLauncher) launcher).getPlayerSettings().getPlayerName());
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
        return frame.isVisible();
    }

    @Override
    public void setVisible(boolean visible)
    {
        frame.setVisible(visible);
    }

    @Override
    public void displayError(String message)
    {
        JOptionPane.showMessageDialog(frame, message, Language.get("errorMessageTitle"), JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void onPercentageChange(final File file, final int overallPercentage, final long fileSize,
            final long fileDownloaded)
    {
        if (updatingStatus)
        {
            return;
        }

        SwingUtilities.invokeLater(new Runnable()
        {

            @Override
            public void run()
            {
                updatingStatus = true;

                String text = Main.lang("downloading") + file.getName();
                StringBuilder sb = new StringBuilder();
                sb.append(overallPercentage);
                sb.append("%");

                if (fileDownloaded > 0 && fileSize > 0)
                {
                    sb.append(", ");
                    sb.append(Util.convertUnit(fileDownloaded));
                    sb.append("B/");
                    sb.append(Util.convertUnit(fileSize));
                    sb.append("B");
                }

                statusLabel.setText(text);
                statusProgressBar.setString(sb.toString());
                statusProgressBar.setValue(overallPercentage);

                updatingStatus = false;
            }
        });
    }

    static
    {
        NEWS_IMAGE_URL = URLUtil.constantURL("http://example.com/news.png");
        REGISTER_URL = Settings.getInstance().getRegisterURL(); // legacy
    }
}
