package com.kalkulator.gui;

import com.kalkulator.core.CalculatorEngine;
import com.kalkulator.ui.Keypad;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/** Aplikasi kalkulator berbasis jendela (Swing). */
public final class GuiApp {

    private static final Color BG = new Color(0x1B1C23);
    private static final Color SCREEN = new Color(0x13141A);
    private static final Color NUM = new Color(0x2C2E3B);
    private static final Color FUNC = new Color(0x3A3D52);
    private static final Color OP = new Color(0xF59E0B);
    private static final Color DANGER = new Color(0xDC2626);
    private static final Color OK = new Color(0x10B981);
    private static final Color MUTED = new Color(0x8B8FA3);

    private static final String ALLOWED_KEYS = "0123456789+-*/^%().=xrnvcd";

    private final CalculatorEngine engine = new CalculatorEngine();
    private JLabel exprLabel;
    private JLabel resultLabel;

    private GuiApp() { }

    /** Membuka jendela kalkulator. */
    public static void launch() {
        if (GraphicsEnvironment.isHeadless()) {
            System.out.println("  GUI tidak tersedia di lingkungan ini. Gunakan mode CLI.");
            return;
        }
        SwingUtilities.invokeLater(() -> new GuiApp().build());
    }

    private void build() {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ignored) {
            // pakai look & feel bawaan
        }

        JFrame frame = new JFrame("Kalkulator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout(0, 16));
        root.setBackground(BG);
        root.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));
        root.add(buildScreen(), BorderLayout.NORTH);
        root.add(buildKeypad(), BorderLayout.CENTER);

        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (c == '\n' || c == '\r') c = '=';
                else if (c == 8) c = 'd';                 // Backspace
                else if (c == 27 || c == 127) c = 'c';    // Esc / Delete
                if (ALLOWED_KEYS.indexOf(Character.toLowerCase(c)) >= 0) {
                    engine.press(c);
                    refresh();
                }
            }
        });

        frame.setContentPane(root);
        frame.setSize(380, 640);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        refresh();
        frame.setVisible(true);
        frame.requestFocusInWindow();
    }

    private JPanel buildScreen() {
        JPanel screen = new JPanel(new GridLayout(2, 1, 0, 4));
        screen.setBackground(SCREEN);
        screen.setBorder(BorderFactory.createEmptyBorder(14, 16, 14, 16));
        screen.setPreferredSize(new Dimension(340, 120));

        exprLabel = new JLabel(" ", SwingConstants.RIGHT);
        exprLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        exprLabel.setForeground(MUTED);

        resultLabel = new JLabel("0", SwingConstants.RIGHT);
        resultLabel.setFont(new Font("Segoe UI", Font.BOLD, 38));
        resultLabel.setForeground(Color.WHITE);

        screen.add(exprLabel);
        screen.add(resultLabel);
        return screen;
    }

    private JPanel buildKeypad() {
        int rows = Keypad.rows();
        int cols = Keypad.cols();
        JPanel pad = new JPanel(new GridLayout(rows, cols, 10, 10));
        pad.setOpaque(false);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                String label = Keypad.label(i, j);
                final char key = Keypad.key(i, j);

                Color c;
                if (label.equals("AC") || label.equals("DEL")) c = DANGER;
                else if (label.equals("=")) c = OK;
                else if (j == cols - 1) c = OP;
                else if (Character.isDigit(label.charAt(0)) || label.equals(".") || label.equals("±")) c = NUM;
                else c = FUNC;

                RoundButton b = new RoundButton(label, c);
                b.addActionListener(e -> {
                    engine.press(key);
                    refresh();
                });
                pad.add(b);
            }
        }
        return pad;
    }

    private void refresh() {
        // Baris ekspresi
        String e = engine.getExpression();
        if (e.length() > 26) e = "…" + e.substring(e.length() - 25);
        exprLabel.setText(e.isEmpty() ? " " : e);

        // Baris hasil
        String txt;
        Color col;
        String result = engine.getResult();
        if (engine.isError()) {
            txt = result;
            col = DANGER;
        } else if (!result.isEmpty()) {
            txt = result.startsWith("= ") ? result.substring(2) : result;
            col = OK;
        } else {
            txt = engine.preview();
            col = Color.WHITE;
        }
        int len = txt.length();
        int size = len > 20 ? 16 : len > 14 ? 22 : len > 10 ? 28 : 38;
        resultLabel.setFont(new Font("Segoe UI", Font.BOLD, size));
        resultLabel.setForeground(col);
        resultLabel.setText(txt);
    }
}
