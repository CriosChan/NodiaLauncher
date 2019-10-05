package fr.criosdu79.nodia.launcher;


import fr.northenflo.auth.exception.DataEmptyException;
import fr.northenflo.auth.exception.DataWrongException;
import fr.northenflo.auth.exception.ServerNotFoundException;
import fr.northenflo.auth.mineweb.AuthMineweb;
import fr.northenflo.auth.mineweb.mc.MinewebGameType;
import fr.northenflo.auth.mineweb.utils.TypeConnection;
import fr.theshark34.openlauncherlib.LaunchException;
import fr.theshark34.openlauncherlib.external.ExternalLauncher;
import fr.theshark34.openlauncherlib.internal.InternalLaunchProfile;
import fr.theshark34.openlauncherlib.internal.InternalLauncher;
import fr.theshark34.openlauncherlib.minecraft.*;
import fr.theshark34.openlauncherlib.minecraft.util.GameDirGenerator;
import fr.theshark34.supdate.BarAPI;
import fr.theshark34.supdate.SUpdate;
import fr.theshark34.supdate.application.integrated.FileDeleter;
import fr.theshark34.swinger.Swinger;

import java.io.File;
import java.io.IOException;

public class Launcher extends LauncherPanel {
    public static final GameVersion MC_VERSION = new GameVersion("1.7.10", MinewebGameType.V1_7_10);
    public static final File N_DIR = GameDirGenerator.createGameDir("NodiaV1");
    public static final GameInfos N_INFOS = new GameInfos("NodiaV1", N_DIR, MC_VERSION, new GameTweak[] {GameTweak.FORGE});

    public static final File N_CRASHES_DIR = new File(N_DIR, "crashes");


    private static AuthInfos authInfos;
    private static Thread updateThread;

    public static void update() throws Exception {
        SUpdate su = new SUpdate("https://crios-game.000webhostapp.com/Minecraft/", N_DIR);
        su.addApplication(new FileDeleter());

        AuthMineweb.setTypeConnection(TypeConnection.launcher);
        AuthMineweb.setUrlRoot("https://nodia-pvp-faction.000webhostapp.com/");
        AuthMineweb.setUsername(usernameField.getText());
        AuthMineweb.setPassword(passwordField.getText());

        try{
            AuthMineweb.start();
        } catch(DataWrongException e){
            e.printStackTrace();
            return;
        } catch(DataEmptyException e){
            e.printStackTrace();
            return;
        } catch (ServerNotFoundException e) {
            e.printStackTrace();
            return;
        } catch (IOException e){
            e.printStackTrace();
            return;
        }
        if(AuthMineweb.isConnected()){
            updateThread = new Thread(){

                int val=0;
                int max=0;
                @Override
                public void run(){
                    while(!this.isInterrupted()){
                        if(BarAPI.getNumberOfFileToDownload() == 0) {
                            LauncherFrame.getInstance().getLauncherPanel().setInfoText("Verification des fichiers...");
                            continue;
                        }

                        val = (int) (BarAPI.getNumberOfTotalDownloadedBytes() / 1000);
                        max = (int) (BarAPI.getNumberOfTotalBytesToDownload() / 1000);
                        LauncherFrame.getInstance().getLauncherPanel().getProgressBar().setMaximum(max);
                        LauncherFrame.getInstance().getLauncherPanel().getProgressBar().setValue(val);
                        LauncherFrame.getInstance().getLauncherPanel().setInfoText("Telechargement des fichiers : " +
                                BarAPI.getNumberOfDownloadedFiles() + " / " + BarAPI.getNumberOfFileToDownload() + " " +
                                Swinger.percentage(val, max) + "%");
                    }
                }
            };updateThread.start();
            su.start();
            updateThread.interrupt();
        }
    }

    public static void launch() throws LaunchException {
        InternalLaunchProfile profile = MinecraftLauncher.createInternalProfile(N_INFOS, GameFolder.BASIC, authInfos);
        InternalLauncher launcher = new InternalLauncher(profile);
        launcher.launch();

        LauncherFrame.getInstance().setVisible(false);
        System.exit(0);
    }


    public static void interruptThread(){
        updateThread.interrupt();
    }

}