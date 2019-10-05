package fr.criosdu79.nodia.launcher;

import fr.theshark34.openlauncherlib.LaunchException;
import fr.theshark34.openlauncherlib.util.Saver;
import fr.theshark34.swinger.Swinger;
import fr.theshark34.swinger.animation.Animator;
import fr.theshark34.swinger.colored.SColoredBar;
import fr.theshark34.swinger.event.SwingerEvent;
import fr.theshark34.swinger.event.SwingerEventListener;
import fr.theshark34.swinger.textured.STexturedButton;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.FilteredImageSource;
import java.io.File;
import java.io.IOException;
import java.lang.Exception;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import static fr.theshark34.swinger.Swinger.getTransparentWhite;
import static javafx.scene.input.DataFormat.URL;

public class LauncherPanel extends JPanel implements SwingerEventListener, ActionListener, ItemListener {

    private Image background = Swinger.getResource("background.png");

    private static Saver saver = new Saver(new File(Launcher.N_DIR, "launcher.properties"));

    public static JTextField usernameField = new JTextField(saver.get("username"));
    public static JTextField passwordField = new JPasswordField(saver.get("password"));

    private STexturedButton playButton = new STexturedButton(Swinger.getResource("play.png"));
    private STexturedButton quitButton = new STexturedButton(Swinger.getResource("quit.png"));
    private STexturedButton hideButton = new STexturedButton(Swinger.getResource("reduction.png"));
    public JCheckBox checkBoxPassword = new JCheckBox(saver.get("passwordCheckBox"));

    java.awt.Font usernameFont = new java.awt.Font("Comic Sans MS", Font.PLAIN,30);

    private SColoredBar progressBar = new SColoredBar(getTransparentWhite(100), getTransparentWhite(175));
    private JLabel infoText = new JLabel("Clique sur Se connecter", SwingConstants.CENTER);
    private JLabel passwordBox = new JLabel("Enregistrer le mot de passe ?");
    private JLabel inscription = new JLabel("Pas encore inscrit ?");

    public LauncherPanel(){
        this.setLayout(null);
        setOpaque(false);

        usernameField.setForeground(Color.black);
        usernameField.setFont(usernameFont);
        usernameField.setCaretColor(Color.black);
        usernameField.setOpaque(false);
        usernameField.setBorder(null);
        usernameField.setBounds(729, 308, 462, 65);
        this.add(usernameField);

        passwordField.setForeground(Color.black);
        passwordField.setFont(passwordField.getFont().deriveFont(20F));
        passwordField.setCaretColor(Color.black);
        passwordField.setOpaque(false);
        passwordField.setBorder(null);
        passwordField.setBounds(729, 467, 462, 65);
        this.add(passwordField);

        playButton.setBounds(724, 704, 462, 100);
        playButton.addEventListener(this);
        this.add(playButton);

        quitButton.setBounds(1870, 4, 41,39);
        quitButton.addEventListener(this);
        this.add(quitButton);

        hideButton.setBounds(1820, 4, 41,39);
        hideButton.addEventListener(this);
        this.add(hideButton);

        progressBar.setBounds(20, 950, 1880, 18);
        this.add(progressBar);

        infoText.setForeground(Color.CYAN);
        infoText.setFont(usernameField.getFont());
        infoText.setBounds(474, 900, 938, 38);
        this.add(infoText);

        checkBoxPassword.setBounds(724, 567, 20, 20);
        checkBoxPassword.addItemListener(this);
        this.add(checkBoxPassword);

        inscription.setFont(usernameField.getFont());
        inscription.setForeground(Color.WHITE);
        inscription.setBounds(724, 627, 938, 40);
        this.add(inscription);

        passwordBox.setFont(usernameField.getFont());
        passwordBox.setForeground(Color.WHITE);
        passwordBox.setBounds(752, 557, 938, 40);
        this.add(passwordBox);

        inscription.addMouseListener(new MouseListener(){
            public void mouseEntered(MouseEvent e){
                inscription.setForeground(Color.RED);
            }
            public void mouseExited(MouseEvent e){
                inscription.setForeground(Color.white);
            }
            public void mouseClicked(MouseEvent e){
                try {
                    Desktop.getDesktop().browse(URI.create("https://nodia-pvp-faction.000webhostapp.com"));
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            public void mouseReleased(MouseEvent e){}
            public void mousePressed(MouseEvent e){}
        });
    }


    @Override
    public void onEvent(SwingerEvent e){
        if(e.getSource() == playButton){
            setFieldsEnabled(false);

            if(usernameField.getText().replaceAll(" ", "").length() == 0 ||  passwordField.getText().length() == 0){
                JOptionPane.showMessageDialog(this, "Erreur, veuillez entrer un pseudo et mot de passe valides.", "Erreur", JOptionPane.ERROR_MESSAGE);
                setFieldsEnabled(true);
                return;
            }

            Thread t = new Thread() {
                @Override
                public void run(){

                    try {
                        Launcher.update();
                    } catch (Exception e) {
                        Launcher.interruptThread();
                        LauncherFrame.getCrashReporter().catchError(e, "Erreur, impossible de lancer NodiaV1");
                        System.exit(1);
                    }

                    saver.set("username", usernameField.getText());

                    try {
                        Launcher.launch();
                    } catch (LaunchException e1) {
                        e1.printStackTrace();
                    }
                }
            };
            t.start();
        } else if(e.getSource() == quitButton){
            Animator.fadeOutFrame(LauncherFrame.getInstance(), 5, new Runnable() {
                @Override
                public void run() {
                    System.exit(0);
                }
            });
        } else if(e.getSource() == hideButton) {
            LauncherFrame.getInstance().setState(JFrame.ICONIFIED);
        }
    }
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(background, 0, 0, this.getWidth(), this.getHeight(), this);
    }

    private void setFieldsEnabled(boolean enabled){
        usernameField.setEnabled(enabled);
        passwordField.setEnabled(enabled);
        playButton.setEnabled(enabled);
        quitButton.setEnabled(enabled);
        passwordBox.setEnabled(enabled);
        checkBoxPassword.setEnabled(enabled);
        inscription.setEnabled(enabled);
    }

    public SColoredBar getProgressBar() {
        return progressBar;
    }

    public void setInfoText(String text){
        infoText.setText(text);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        Object source = e.getItemSelectable();

        if(source == checkBoxPassword){
            saver.set("password", passwordField.getText());
            saver.set("passwordCheckBox", "true");
        } else {
            saver.set("password", "");
        }
    }
}
