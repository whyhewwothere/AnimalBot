package why.hello.there;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.net.HttpURLConnection;
import java.net.URL;

public class Main {
    public static String catApiKey = "API_KEY_HERE";
    public static String dogApiKey = "API_KEY_HERE";
    private static final String botToken = "API_KEY_HERE";
    public static JDA jda;
    public static void main(String[] args) throws InterruptedException {
        jda = JDABuilder
                .createDefault(botToken)
                .enableIntents(GatewayIntent.GUILD_MEMBERS)
                .build();
        jda.awaitReady();
        jda.upsertCommand("cat", "Get a random cat image").queue();
        jda.upsertCommand("dog", "Get a random dog image").queue();
        jda.addEventListener(new JoinListener());
        jda.addEventListener(new CommandListener());
        jda.addEventListener(new ButtonListener());
    }

    public static String getImage(boolean useDog) {
        try {
            String apiUrl = "https://api.the" + (useDog ? "dog" : "cat") + "api.com/v1/images/search?format=src";
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.addRequestProperty("x-api-key", useDog ? dogApiKey : catApiKey);
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                String newUrl = connection.getURL().toString();
                connection.disconnect();
                return newUrl;
            } else {
                connection.disconnect();
                return "Error: Unable to fetch image. Response code: " + responseCode;
            }

        } catch (Exception err) {
            err.printStackTrace();
        }
        return "Error: Unable to fetch image.";
    }
}