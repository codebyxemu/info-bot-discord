package me.xemu.testbot.handlers;

import me.xemu.testbot.types.Embed;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
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
public class CommandStaffQuestion extends ListenerAdapter {

	@Override
	public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
		if (event.getName().equalsIgnoreCase("question")) {
			event.replyModal(
					Modal.create("question-modal", "Send us your question")
							.addComponents(
									ActionRow.of(
											TextInput.create("question-modal:question", "Question", TextInputStyle.SHORT)
													.setPlaceholder("What is your question?")
													.setMaxLength(1000)
													.build()
									)
							)
							.build()
			).queue();
		}
	}

	@Override
	public void onModalInteraction(@NotNull ModalInteractionEvent event) {
		if (event.getModalId().equalsIgnoreCase("question-modal")) {
			String question = event.getValue("question-modal:question").getAsString();

			TextChannel channel = event.getJDA().getGuildById("1133115083279564910").getTextChannelById("1134478583126048860");

			channel.sendMessageEmbeds(
					new Embed("Question received from " + event.getUser().getEffectiveName(), true)
							.text("```" + question + "```")
							.build()
			).addActionRow(Button.secondary("reply-" + event.getUser().getId(), "Reply").withEmoji(Emoji.fromUnicode("📩")))
					.queue(message -> {
						event.reply("Your question has been sent to the staff team.").setEphemeral(true).queue();
					});
		} else if (event.getModalId().startsWith("modal-reply-")) {
			String replyTo = event.getModalId().replace("modal-reply-", "");
			User user = event.getJDA().getUserById(replyTo);

			if (user == null) {
				event.reply("This user is not in the server.").setEphemeral(true).queue();
				return;
			}

			String reply = event.getValue("modal-reply").getAsString();
			user.openPrivateChannel().queue(c -> {
				c.sendMessageEmbeds(
						new Embed("Reply received", true)
								.text("```" + reply + "```")
								.build()
				).setActionRow(Button.secondary("new-question", "New Question")).queue();
			});

			event.reply("Your reply was forwarded to the user who asked.").setEphemeral(true).queue();
		}
	}

	@Override
	public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
		if (event.getButton().getId().startsWith("reply-")) {
			String replyTo = event.getButton().getId().replace("reply-", "");

			event.replyModal(
					Modal.create("modal-reply-" + replyTo, "Send us your question")
							.addComponents(
									ActionRow.of(
											TextInput.create("modal-reply", "Reply", TextInputStyle.SHORT)
													.setPlaceholder("What is your reply?")
													.setMaxLength(1000)
													.build()
									)
							)
							.build()
			).queue();
		} else if (event.getButton().getId().equalsIgnoreCase("new-question")) {
			event.replyModal(
					Modal.create("question-modal", "Send us your question")
							.addComponents(
									ActionRow.of(
											TextInput.create("question-modal:question", "Question", TextInputStyle.SHORT)
													.setPlaceholder("What is your question?")
													.setMaxLength(1000)
													.build()
									)
							)
							.build()
			).queue();
		}
	}
}
