package me.xemu.testbot.handlers;

import me.xemu.testbot.types.Embed;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
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
public class CommandServerData extends ListenerAdapter {

	@Override
	public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
		if (event.getName().equalsIgnoreCase("serverdata")) {
			Guild guild = event.getGuild();

			String owner = guild.getOwner().getAsMention();
			String members = String.valueOf(guild.getMemberCount());
			String roles = String.valueOf(guild.getRoles().size());
			String channels = String.valueOf(guild.getChannels().size());
			String emojis = String.valueOf(guild.getEmojis().size());

			String createdAt = guild.getTimeCreated().format(DateTimeFormatter.ISO_LOCAL_DATE);
			String booster = guild.getBoosters().isEmpty() ? "No boosters! :(" : guild.getBoosters().stream().map(Member::getAsMention).collect(Collectors.joining(", ")) + " (total: " + guild.getBoostCount() + ")";

			String summary = "```" +
					"Members: " + members + "\n" +
					"Roles: " + roles + "\n" +
					"Channels: " + channels + "\n" +
					"Emojis: " + emojis + "\n" +
					"Boosters: " + booster + "\n" +
					"```";

			MessageEmbed embed = new Embed("Server Data: " + guild.getName(), true)
					.text("You are viewing guild information for " + guild.getName() + ".")
					.field("Owner", owner, true)
					.field("Created at", createdAt, true)
					.field("Guild Statistics", summary, false)
					.thumbnail(guild.getIconUrl())
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
