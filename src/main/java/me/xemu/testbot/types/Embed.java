package me.xemu.testbot.types;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;

import java.awt.*;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class Embed extends EmbedBuilder {

	public Embed() {
		color(new Color(81, 153, 226));
	}

	public Embed(String title) {
		if(title != null)
			setAuthor(title, null,"https://media.discordapp.net/attachments/1134147111273902180/1136989606638784542/Zeppelin_Transparent_Icon.png?width=904&height=904");

		color(new Color(81, 153, 226));
		footer("Developed by Xemu");
	}

	public Embed(String title, boolean footer) {
		if(title != null)
			setAuthor(title, null,"https://media.discordapp.net/attachments/1134147111273902180/1136989606638784542/Zeppelin_Transparent_Icon.png?width=904&height=904");
		if(footer)
			footer("Developed by Xemu");

		color(new Color(81, 153, 226));
	}

	public Embed(boolean footer) {
		if(footer)
			footer("Developed by Xemu");

		color(new Color(81, 153, 226));
	}

	public Embed footer(String text) {
		setFooter("Test Bot • " + text, "https://media.discordapp.net/attachments/1134147111273902180/1136989606638784542/Zeppelin_Transparent_Icon.png?width=904&height=904");

		return this;
	}

	public Embed error() {
		color(new Color(178,34,34));

		return this;
	}

	public Embed success() {
		color(new Color(50, 205, 50));

		return this;
	}

	public Embed text(String text) {
		setDescription(text);

		return this;
	}

	public Embed text(String... text) {
		setDescription(String.join("\n", text));

		return this;
	}

	public Embed thumbnail(String url) {
		super.setThumbnail(url);

		return this;
	}

	public Embed color(Color color) {
		if(color == null)
			return this;

		super.setColor(color);
		return this;
	}

	public Embed image(String url) {
		super.setImage(url);

		return this;
	}

	public Embed field(String name, String value, boolean inline) {
		super.addField(name, value, inline);

		return this;
	}

	public Embed blankField(boolean inline) {
		super.addBlankField(inline);

		return this;
	}

	public String getText() {
		return super.getDescriptionBuilder().toString();
	}

	@Deprecated
	// Try not to use this as it sleeps the bot basically.
	public Message complete(TextChannel textChannel) {
		return textChannel.sendMessageEmbeds(build()).complete();
	}

	@Deprecated
	// Try not to use this as it sleeps the bot basically.
	public Message complete(Member member) {
		return complete(member.getUser());
	}

	public Message complete(User user) {
		try {
			return user.openPrivateChannel().complete().sendMessageEmbeds(build()).complete();
		} catch (ErrorResponseException ignore) { }
		return null;
	}

	public void queue(TextChannel textChannel) {
		textChannel.sendMessageEmbeds(build()).queue();
	}

	public void queue(Member member) {
		queue(member.getUser());
	}

	public void queue(Member member, Consumer<Message> consumer) {
		queue(member.getUser(), consumer);
	}

	public void queue(User user) {
		try {
			user.openPrivateChannel().submit()
					.thenCompose(channel -> channel.sendMessageEmbeds(build()).submit())
					.whenComplete((message, error) -> {
						if (error != null){
							System.out.println("[Zeppelin] Could not send private message to " + user.getName());
						}
					});
		} catch (Exception ignore) { }
	}

	public void queue(User user, Consumer<Message> consumer) {
		try {
			user.openPrivateChannel().queue(c -> c.sendMessageEmbeds(build()).queue(consumer));
		} catch (Exception ignore) { }
	}

	public void queue(TextChannel textChannel, Consumer<Message> consumer) {
		textChannel.sendMessageEmbeds(build()).queue(consumer);
	}

	public void queueAfter(TextChannel textChannel, int delay, TimeUnit unit) {
		textChannel.sendMessageEmbeds(build()).queueAfter(delay, unit);
	}

	public void queueAfter(TextChannel textChannel, int delay, TimeUnit unit, Consumer<Message> success) {
		textChannel.sendMessageEmbeds(build()).queueAfter(delay, unit, success);
	}

	public void queueAfter(User user, int delay, TimeUnit time) {
		try {
			user.openPrivateChannel().complete().sendMessageEmbeds(build()).queueAfter(delay, time);
		} catch (ErrorResponseException ignore) { }
	}

	public Message reply(Message message) {
		return reply(message, true);
	}

	public Message reply(Message message, boolean mention) {
		return message.replyEmbeds(build()).mentionRepliedUser(mention).complete();
	}

	public void replyTemporary(Message message, int duration, TimeUnit timeUnit) {
		replyTemporary(message, true, duration, timeUnit);
	}

	public void replyTemporary(Message message, boolean mention, int duration, TimeUnit timeUnit) {
		message.replyEmbeds(build()).mentionRepliedUser(mention).queue((msg -> msg.delete().submitAfter(duration, timeUnit)));
	}

	public void sendTemporary(TextChannel textChannel, int duration, TimeUnit timeUnit) {
		queue(textChannel, (msg) -> msg.delete().submitAfter(duration, timeUnit));
	}

	public void sendTemporary(TextChannel textChannel, int duration) {
		sendTemporary(textChannel, duration, TimeUnit.SECONDS);
	}

	public ScheduledFuture<?> sendAfter(TextChannel textChannel, int duration, Consumer<Message> onSuccess) {
		return textChannel.sendMessageEmbeds(build()).queueAfter(duration, TimeUnit.SECONDS, onSuccess);
	}

	public ScheduledFuture<?> sendAfter(TextChannel textChannel, int duration, TimeUnit timeUnit, Consumer<Message> onSuccess) {
		return textChannel.sendMessageEmbeds(build()).queueAfter(duration, timeUnit, onSuccess);
	}
}