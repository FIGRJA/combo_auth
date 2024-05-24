package org.figrja.combo_auth;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.figrja.combo_auth.config.configGson;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;


public class auth implements DedicatedServerModInitializer {

    static configGson config;
    private static final Gson gson = new Gson();

    private static final org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger("combo_auth");

    @Override
    public void onInitializeServer() {

        File ConfFile = FabricLoader.getInstance().getConfigDir().resolve( "combo_auth.json" ).toFile();
        JsonReader reader = null;
        try {
            reader = new JsonReader(new FileReader(ConfFile));
        } catch (FileNotFoundException e) {
            try {
                Files.createFile(ConfFile.toPath());
                InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("combo_auth.json");
                reader = new JsonReader(
                        new BufferedReader(
                                new InputStreamReader(
                                        inputStream)));

                PrintWriter printWriter = new PrintWriter(ConfFile);
                Scanner scanner = new Scanner(inputStream);
                while (scanner.hasNextLine()){
                    printWriter.println(scanner.nextLine());
                }
                scanner.close();
                printWriter.flush();
                printWriter.close();
                inputStream.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }

        config = gson.fromJson(reader,configGson.class);

        LOGGER.info("combo_auth has been enabled!");

    }

    public static configGson getConfig() {
        return config;
    }
}
