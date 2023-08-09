package me.xemu.testbot.handlers;

import me.xemu.testbot.types.Embed;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;

import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

/**
 * Xemu Development
 * A part of Magnus TK Media (https://magnustk.com/)
 *
 * @author Magnus T. Kristensen
 * @mailto magnusdevofficial@gmail.com
 **/
public class CommandUserData extends ListenerAdapter {

	@Override
	public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
		if (event.getName().equalsIgnoreCase("userdata")) {

			Member discordUser = event.getOption("user").getAsMember();

			if (discordUser == null) {
				event.replyEmbeds(
						new Embed(
								"Could not get user data",
								true
						).error().text("This user is not in our server.").build()
				).setEphemeral(true).queue();
				return;
			}

			String createdAt = discordUser.getTimeCreated().format(DateTimeFormatter.ISO_LOCAL_DATE);
			String joinedAt = discordUser.getTimeJoined().format(DateTimeFormatter.ISO_LOCAL_DATE);
			String roles = discordUser.getRoles().isEmpty() ? "No roles" : discordUser.getRoles().stream().map(role -> role.getAsMention()).collect(Collectors.joining(", "));
			String booster = discordUser.isBoosting() ? "Since " + discordUser.getTimeBoosted().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) : "No";

			MessageEmbed embed = new Embed("User Data: " + discordUser.getEffectiveName(), true)
					.text("You are viewing user information for " + discordUser.getAsMention() + ".")
					.field("Created at", createdAt, true)
					.field("Joined at", joinedAt, true)
					.field("Roles", roles, false)
					.field("Server Booster", booster, false)
					.thumbnail(discordUser.getUser().getAvatarUrl())
					.build();


			event.replyEmbeds(embed).setActionRow(Button.danger("delete", Emoji.fromUnicode("\uD83D\uDDD1\uFE0F"))).queue();
		}
	}

	@Override
	public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
		if (event.getButton().getId().equalsIgnoreCase("delete")) {
			try {
				event.getMessage().delete().queue();
			} catch (Exception e) {
				event.reply("Could not delete message.").setEphemeral(true).queue();
			}
		}
	}
}
