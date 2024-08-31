package me.lowlauch.walorestartcompanion;

import java.io.File;
import java.io.IOException;

// Here I present this cobbled together code of deleting worlds and restarting the server
// I could not find a way to do this inside the spigot plugin
public class Main {
    private static void deleteDir(File file, boolean firstIteration) {
        System.out.println(file.getPath());

        if (firstIteration) {
            File sessionLock = new File(file.getPath() + "/session.lock");
            if (!sessionLock.exists()) {
                System.out.println("Folder does not contain session.lock! It is NOT a Minecraft world folder!");
                return;
            }
        }

        File[] contents = file.listFiles();
        if (contents != null) {
            for (File f : contents) {
                deleteDir(f, false);
            }
        }

        file.delete();
    }

    public static void restartServer() throws IOException {
        if (System.getProperty("os.name").toLowerCase().contains("windows")) {
            String path = "cmd /c start \"\" start.bat";
            Process process = Runtime.getRuntime().exec(path);
        } else {
            // Probably UNIX-like Operating System
            String[] cmd = new String[]{"/bin/bash", "start.sh"};
            Process process = Runtime.getRuntime().exec(cmd);
        }
    }

    public static void deleteWorld(String worldPath) {
        deleteDir(new File(worldPath), true);
    }

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.out.println("You need to specify a world folder path or '--restart'!");
            return;
        }

        for (String argument : args) {
            if (argument.equals("--restart")) {
                restartServer();
            } else {
                deleteWorld(argument);
            }
        }
    }
}