package me.xemu.testbot;

import com.mongodb.client.MongoClients;
import de.leonhard.storage.Yaml;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import lombok.Getter;
import lombok.SneakyThrows;
import me.xemu.testbot.handlers.CommandServerData;
import me.xemu.testbot.handlers.CommandStaffQuestion;
import me.xemu.testbot.handlers.CommandUserData;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

/**
 * Xemu Development
 * A part of Magnus TK Media (https://magnustk.com/)
 *
 * @author Magnus T. Kristensen
 * @mailto magnusdevofficial@gmail.com
 **/
public class Bot {

	@Getter
	private static JDA jda;
	private static Bot instance;

	@Getter
	private static Datastore datastore;

	private static Yaml config = new Yaml("config", "./");

	@SneakyThrows
	public Bot() {
		instance = this;
		jda = JDABuilder.createDefault(config.getOrSetDefault("token", "TOKEN_HERE"))
				.setEnabledIntents(GatewayIntent.getIntents(GatewayIntent.DEFAULT | GatewayIntent.GUILD_MEMBERS.getRawValue() | GatewayIntent.GUILD_BANS.getRawValue()))
				.setDisabledIntents(GatewayIntent.DIRECT_MESSAGE_TYPING, GatewayIntent.GUILD_MESSAGE_TYPING)
				.enableCache(CacheFlag.ONLINE_STATUS)
				.disableCache(CacheFlag.ACTIVITY, CacheFlag.VOICE_STATE)
				.setMemberCachePolicy(MemberCachePolicy.ALL)
				.setChunkingFilter(ChunkingFilter.ALL)
				.setActivity(Activity.watching(
						config.getOrSetDefault("activity", "Bot Test")
				))
				.build().awaitReady();

		jda.addEventListener(new CommandUserData(), new CommandServerData(), new CommandStaffQuestion());

		jda.upsertCommand("userdata", "View user information.").addOption(OptionType.USER, "user", "The user to view information about.", true).queue();
		jda.upsertCommand("serverdata", "View server information.").queue();
		jda.upsertCommand("question", "Send us a question.").queue();

		datastore = Morphia.createDatastore(
				MongoClients.create(
						config.getOrSetDefault("db.url", "MONGO_URL")
				),
				config.getOrSetDefault("db.name", "DB_NAME")
		);
	}

	public static void main(String[] args) {
		new Bot();
	}

}
