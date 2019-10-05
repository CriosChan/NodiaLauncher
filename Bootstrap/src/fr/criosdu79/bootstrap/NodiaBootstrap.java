package fr.criosdu79.bootstrap;

import fr.theshark34.openlauncherlib.LaunchException;
import fr.theshark34.openlauncherlib.external.ClasspathConstructor;
import fr.theshark34.openlauncherlib.external.ExternalLaunchProfile;
import fr.theshark34.openlauncherlib.external.ExternalLauncher;
import fr.theshark34.openlauncherlib.minecraft.util.GameDirGenerator;
import fr.theshark34.openlauncherlib.util.CrashReporter;
import fr.theshark34.openlauncherlib.util.SplashScreen;
import fr.theshark34.openlauncherlib.util.explorer.ExploredDirectory;
import fr.theshark34.openlauncherlib.util.explorer.Explorer;
import fr.theshark34.openlauncherlib.util.ramselector.RamSelector;
import fr.theshark34.supdate.BarAPI;
import fr.theshark34.supdate.SUpdate;
import fr.theshark34.swinger.Swinger;
import fr.theshark34.swinger.colored.SColoredBar;

import java.io.File;
import java.util.Arrays;

import static fr.theshark34.swinger.Swinger.getTransparentWhite;

public class NodiaBootstrap {

    private static final File N_DIR = GameDirGenerator.createGameDir("NodiaV1");
    public static CrashReporter errorUtil = new CrashReporter("NodiaV1", new File(N_DIR, "crashes"));

    private static SplashScreen splash;
    private static Thread barThread;

    private static RamSelector ramSelector = new RamSelector(new File(N_DIR, "ram.txt"));
    public static void main(String[] args){
        Swinger.setResourcePath("/fr/criosdu79/bootstrap/ressources");
        displaySplash();
        try{
            doUpdate();
        } catch (Exception e){
            errorUtil.catchError(e, "Impossible de mettre à jour le launcher");
            barThread.interrupt();
        }
        ramSelector.setFrameClass(NodiaRamSelectorWindow.class);
        ramSelector.display();
    }

    private static void displaySplash (){
        splash = new SplashScreen("NodiaV1", Swinger.getResource("splash.png"));

        splash.setVisible(true);
    }

    private static void doUpdate() throws Exception{
        SUpdate su = new SUpdate("https://crios-game.000webhostapp.com/bootstrap", new File(N_DIR, "Launcher"));

        su.start();
    }

    static void launch() throws LaunchException {
        ClasspathConstructor constructor = new ClasspathConstructor();
        ExploredDirectory gameDir = Explorer.dir(N_DIR);
        constructor.add(gameDir.sub("Launcher/Libs").allRecursive().files().match("^(.*\\.((jar)$))*$"));
        constructor.add(gameDir.get("Launcher/launcher.jar"));

        ExternalLaunchProfile profile = new ExternalLaunchProfile("fr.criosdu79.nodia.launcher.LauncherFrame", constructor.make());
        profile.setVmArgs(Arrays.asList(ramSelector.getRamArguments()));

        ExternalLauncher launcher = new ExternalLauncher(profile);

        Process p = launcher.launch();
        System.exit(0);

        try {
            p.waitFor();
        } catch (InterruptedException ignored){
        }
    }

}
