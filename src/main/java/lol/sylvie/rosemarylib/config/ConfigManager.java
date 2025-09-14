package lol.sylvie.rosemarylib.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import lol.sylvie.rosemarylib.Rosemary;
import lombok.Getter;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ConfigManager<T extends ConfigManager.Instance> {
    public static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    private final Logger logger;

    @Getter
    private T instance;

    protected File file;

    public ConfigManager(String modId, T instance) {
        this.file = FabricLoader.getInstance().getConfigDir().resolve(modId + ".json").toFile();
        this.logger = LoggerFactory.getLogger(Rosemary.MOD_ID + "/" + modId);

        this.instance = instance;

        this.read(file);
    }

    @SuppressWarnings("unchecked")
    public void read(File file) {
        try (FileReader reader = new FileReader(file)) {
            this.instance = (T) GSON.fromJson(reader, instance.getClass());
        } catch (IOException | JsonSyntaxException readException) {
            logger.warn("Error while reading config:", readException);
            this.write(file);
        }
    }

    public void write(File file) {
        try (FileWriter writer = new FileWriter(file)) {
            GSON.toJson(instance, writer);
        } catch (IOException writeException) {
            logger.error("Error while writing config:", writeException);
        }
    }

    public void addSaveShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            this.write(file);
        }));
    }

    public static class Instance {}
}
