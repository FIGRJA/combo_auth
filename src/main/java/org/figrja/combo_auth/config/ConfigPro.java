package org.figrja.combo_auth.config;

import net.fabricmc.fabric.impl.resource.loader.FabricModResourcePack;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.commons.io.output.WriterOutputStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ConfigPro {

    private static final Logger LOGGER = LogManager.getLogger("combo_auth");

    private Map<String,String> config = new HashMap<>();

    private final File f;

    public ConfigPro(String name){
        Path path = FabricLoader.getInstance().getConfigDir();

        f = path.resolve( name + ".properties" ).toFile();

        if( !f.exists() ) {
            LOGGER.info( f.getName() + " is missing, generating default config..." );

            try {
                //f.getParentFile().mkdirs();
                Files.createFile( f.toPath() );
                Default();
            } catch (IOException e) {
                LOGGER.error( f.getName() + " failed to generate!" );
                LOGGER.trace( e );
            }
        }
        parse();




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
                    config.put(split[0], split[1]);
                }
            }
            scanner.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void Default(){
        try {
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("config.properties");
            PrintWriter printWriter = new PrintWriter(f);
            Scanner scanner = new Scanner(inputStream);
            while (scanner.hasNextLine()){
                printWriter.println(scanner.nextLine());
            }
            scanner.close();
            printWriter.flush();
            printWriter.close();
            inputStream.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Boolean isBoolean(String key){
        return Boolean.valueOf(config.get(key));
    }

    public String isString(String key){
        return config.get(key);
    }
}
