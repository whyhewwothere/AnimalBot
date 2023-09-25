package why.hello.there;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ButtonListener extends ListenerAdapter {
    @Override
    public void onButtonInteraction(ButtonInteractionEvent e) {
        if (!e.getComponentId().startsWith("vote")) return;
        String[] parts = e.getComponentId().split("-");
        if (!parts[2].equals(e.getUser().getId())) {
            e.reply("You cannot vote on someone else's image!").setEphemeral(true).queue();
            return;
        }
        boolean useDog = parts[1].equals("dog");
        try {
            String apiUrl = useDog ? "https://api.thedogapi.com/v1/votes" : "https://api.thecatapi.com/v1/votes";
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("x-api-key", useDog ? Main.dogApiKey : Main.catApiKey);
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");

            String jsonData = "{\"image_id\": \"" + parts[3] + "\", \"value\": " + (parts[4].equals("up") ? 1 : -1) + "}";

            OutputStream os = connection.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os, StandardCharsets.UTF_8);
            osw.write(jsonData);
            osw.flush();
            osw.close();

            int responseCode = connection.getResponseCode();
            if (responseCode != 201) {
                connection.disconnect();
                e.reply("Error: Unable to cast vote. Response code: " + responseCode).queue();
                return;
            }
            connection.disconnect();
        } catch (Exception err) {
            err.printStackTrace();
        }
        List<Button> buttons = e.getMessage().getButtons();
        e.getMessage().editMessageComponents().setActionRow(buttons.get(0).asDisabled(), buttons.get(1).asDisabled()).queue();
        e.reply("Your " + parts[4] + "vote has been casted!").queue();
    }
}
