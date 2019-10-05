package fr.criosdu79.nodia.launcher;

import com.sun.awt.AWTUtilities;
import fr.theshark34.openlauncherlib.util.CrashReporter;
import fr.theshark34.swinger.Swinger;
import fr.theshark34.swinger.animation.Animator;
import fr.theshark34.swinger.util.WindowMover;

import javax.swing.*;

public class LauncherFrame extends JFrame{

    private static LauncherFrame instance;
    private LauncherPanel LauncherPanel;
    private static CrashReporter crashReporter;

    public LauncherFrame(){
        this.setTitle("Nodia V1");
        this.setSize(1920, 1001);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setUndecorated(true);
        this.setIconImage(Swinger.getResource("icon.png"));
        this.setContentPane(LauncherPanel = new LauncherPanel());
        AWTUtilities.setWindowOpacity(this, 0.0F);

        WindowMover mover = new WindowMover(this);
        this.addMouseListener(mover);
        this.addMouseMotionListener(mover);

        this.setVisible(true);

        Animator.fadeInFrame(this, Animator.FAST);

    }

    public static void main(String[] srgs){
        Swinger.setSystemLookNFeel();
        Swinger.setResourcePath("/fr/criosdu79/nodia/launcher/ressource");
        Launcher.N_CRASHES_DIR.mkdirs();
        crashReporter = new CrashReporter("NodiaV1", Launcher.N_CRASHES_DIR);

        instance = new LauncherFrame();
    }

    public static LauncherFrame getInstance(){
        return instance;
    }

    public static CrashReporter getCrashReporter(){
        return crashReporter;
    }

    public LauncherPanel getLauncherPanel(){
        return this.LauncherPanel;
    }
}
