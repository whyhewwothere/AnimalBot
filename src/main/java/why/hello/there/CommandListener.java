package why.hello.there;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;

import static why.hello.there.Main.getImage;

public class CommandListener extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent e) {
        if (!e.getName().equals("cat") && !e.getName().equals("dog")) return;
        String animal = e.getName();
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Have yourself a " + animal + "!");
        String url = getImage(animal.equals("dog"));
        if (url.contains("Error")) {
            embed.setDescription(url);
            e.replyEmbeds(embed.build()).queue();
            return;
        }
        embed.setImage(url);
        String ids = "vote-" + animal + "-" + e.getUser().getId() + "-" + url.replace("https://cdn2.the" + animal + "api.com/images/", "").split("\\.")[0] + "-";
        e.replyEmbeds(embed.build()).addActionRow(Button.primary(ids + "up", "Upvote"), Button.danger(ids + "down", "Downvote")).queue();
    }
}
