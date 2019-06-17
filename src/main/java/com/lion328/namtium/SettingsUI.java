package com.lion328.namtium;

import com.lion328.hydra.ImagePanel;
import com.lion328.hydra.Language;
import com.lion328.hydra.PlayerSettings;
import com.lion328.hydra.PlayerSettingsUI;
import com.lion328.hydra.TooLowMaximumMemoryException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class SettingsUI implements PlayerSettingsUI
{

    private PlayerSettings playerSettings;

    private JDialog dialog;
    private JFrame mainFrame;
    private JTextField memoryField;

    public SettingsUI(JFrame mainFrame)
    {
        this.mainFrame = mainFrame;

        init();
    }

    @Override
    public void setPlayerSettings(PlayerSettings settings)
    {
        playerSettings = settings;

        refresh();
    }

    private void refresh()
    {
        memoryField.setText(Integer.toString(playerSettings.getMaximumMemory()));
    }

    private void init()
    {
        ImagePanel panel = new ImagePanel();

        try
        {
            panel.setImage(
                    ImageIO.read(getClass().getResourceAsStream("/com/lion328/namtium/resources/settings_bg.png")));
        }
        catch (IOException e)
        {
            Main.getLogger().catching(e);
        }

        panel.setPreferredSize(new Dimension(400, 150));

        Border border = BorderFactory.createEmptyBorder(0, 5, 0, 5);

        memoryField = new JTextField();
        memoryField.setBounds(178, 14, 206, 28);
        memoryField.setBorder(border);
        memoryField.setHorizontalAlignment(SwingConstants.RIGHT);
        panel.add(memoryField);

        JPanel saveButton = new JPanel();
        saveButton.setBounds(259, 66, 60, 60);
        saveButton.setOpaque(false);
        saveButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        saveButton.addMouseListener(new MouseAdapter()
        {

            @Override
            public void mouseClicked(MouseEvent mouseEvent)
            {
                save();
            }
        });
        panel.add(saveButton);

        JPanel exitButton = new JPanel();
        exitButton.setBounds(324, 66, 60, 60);
        exitButton.setOpaque(false);
        exitButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        exitButton.addMouseListener(new MouseAdapter()
        {

            @Override
            public void mouseClicked(MouseEvent mouseEvent)
            {
                setVisible(false);
            }
        });
        panel.add(exitButton);

        dialog = new JDialog(mainFrame, Language.get("settingsTitle"), true);
        dialog.setLocationRelativeTo(null);
        dialog.setResizable(false);
        dialog.setContentPane(panel);
        dialog.pack();
    }

    private void save()
    {
        int memory;

        try
        {
            memory = Integer.valueOf(memoryField.getText());
        }
        catch (NumberFormatException ignore)
        {
            JOptionPane.showMessageDialog(dialog, Main.lang("errorValidateMemory"), Language.get("errorMessageTitle"),
                    JOptionPane.ERROR_MESSAGE);

            return;
        }

        try
        {
            playerSettings.setMaximumMemory(memory);
        }
        catch (TooLowMaximumMemoryException e)
        {
            JOptionPane.showMessageDialog(dialog,
                    String.format(Main.lang("errorTooLowMemory"), e.getMinimumMemoryInMB()),
                    Language.get("errorMessageTitle"),
                    JOptionPane.ERROR_MESSAGE);
        }

        setVisible(false);
    }

    @Override
    public void setVisible(boolean visible)
    {
        if (visible)
        {
            refresh();
            dialog.setLocationRelativeTo(mainFrame);
        }

        dialog.setVisible(visible);
    }

    @Override
    public boolean isVisible()
    {
        return dialog.isVisible();
    }
}
