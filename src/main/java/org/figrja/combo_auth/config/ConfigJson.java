package org.figrja.combo_auth.config;

import com.google.gson.Gson;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.figrja.combo_auth.data.uuidBase;

public class ConfigJson {
    private static final Logger LOGGER = LogManager.getLogger("combo_auth");

    File f;

    static uuidBase uuidBase;

    public ConfigJson(String name) {
        Path path = FabricLoader.getInstance().getConfigDir();

        f = path.resolve( name + ".properties" ).toFile();

        if( !f.exists() ) {
            LOGGER.info( f.getName() + " is missing, generating default one..." );

            try {
                f.getParentFile().mkdirs();
                Files.createFile( f.toPath() );
            } catch (IOException e) {
                LOGGER.error( f.getName() + " failed to generate!" );
                LOGGER.trace( e );
            }
        }
        else {
            parse();

        }

    }

    private void parse() {
        try {
            Scanner scanner = new Scanner(f);
            while (scanner.hasNextLine()){
                String next = scanner.nextLine();
                if (next.startsWith("#")){
                    continue;
                }
                String[] split = next.split("=");
                if (split.length == 2) {
                    String[] splid = split[1].split(":");
                    if(splid.length == 2){
                        UUID[] uid = new UUID[2];
                        uid[0] = splid[0].equals("null") ? null : UUID.fromString(splid[0]);
                        uid[1] = splid[1].equals("null") ? null : UUID.fromString(splid[1]);
                        uuidBase.baseUUID.put(UUID.fromString(split[0]),uid);
                    }
                }
            }
            scanner.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setJsonSans(uuidBase uuidBase){
        try {
            PrintWriter writer = new PrintWriter(f);
            for (Map.Entry<UUID, UUID[]> entry : uuidBase.baseUUID.entrySet()) {
                String[] uid = new String[2];
                uid[0] = entry.getValue()[0]==null?"null":entry.getValue()[0].toString();
                uid[1] = entry.getValue()[1]==null?"null":entry.getValue()[1].toString();
                writer.println(entry.getKey().toString()+"="+uid[0]+":"+uid[1]);
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public uuidBase getUuidBase() {
        return uuidBase;
    }
}
