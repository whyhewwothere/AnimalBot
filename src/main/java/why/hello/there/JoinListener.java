package why.hello.there;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.util.Random;

import static why.hello.there.Main.getImage;

public class JoinListener extends ListenerAdapter {
    Random random = new Random();
    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent e) {
        e.getUser().openPrivateChannel().queue(channel -> {
            EmbedBuilder embed = new EmbedBuilder();
            embed.setTitle("Welcome to the server, " + e.getUser().getName() + "!");
            boolean useDog = random.nextBoolean();
            String animal = useDog ? "dog" : "cat";
            String url = getImage(useDog);
            if (url.contains("Error")) {
                embed.setDescription(url);
                channel.sendMessageEmbeds(embed.build()).queue();
                return;
            }
            embed.setDescription("We hope you enjoy your stay here, and have a " + animal + "!");
            embed.setImage(url);
            String ids = "vote-" + animal + "-" + e.getUser().getId() + "-" + url.replace("https://cdn2.the" + animal + "api.com/images/", "").split("\\.")[0] + "-";
            channel.sendMessageEmbeds(embed.build()).addActionRow(Button.primary(ids + "up", "Upvote"), Button.danger(ids + "down", "Downvote")).queue();
        });
    }
}
