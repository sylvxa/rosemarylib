package lol.sylvie.testmod.config;

import com.google.gson.annotations.SerializedName;
import lol.sylvie.rosemarylib.config.ConfigManager;
import lombok.Getter;

@Getter
public class ExampleConfig extends ConfigManager.Instance {
    @SerializedName("welcome_message")
    public String welcomeMessage = "Welcome to the server!";
}
