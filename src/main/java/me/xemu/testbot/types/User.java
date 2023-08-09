package me.xemu.testbot.types;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.query.MorphiaCursor;
import dev.morphia.query.Query;
import dev.morphia.query.filters.Filter;
import dev.morphia.query.filters.Filters;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.xemu.testbot.Bot;

import java.util.ArrayList;
import java.util.List;

/**
 * Xemu Development
 * A part of Magnus TK Media (https://magnustk.com/)
 *
 * @author Magnus T. Kristensen
 * @mailto magnusdevofficial@gmail.com
 **/
@Entity("users")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class User {

	@Id
	private String id;

	private String username;

	public static User get(String id) {
		Filter filter = Filters.eq("id", id);
		Query<User> query = Bot.getDatastore().find(User.class)
				.filter(filter);

		MorphiaCursor<User> cursor = query.iterator();

		if (cursor.hasNext()) {
			return cursor.next();
		}

		return null;
	}


	public static List<User> getAll() {
		Query<User> query = Bot.getDatastore().find(User.class);

		MorphiaCursor<User> cursor = query.iterator();
		List<User> userList = new ArrayList<>();

		while (cursor.hasNext()) {
			userList.add(cursor.next());
		}

		return userList;
	}

	public void save() {
		Bot.getDatastore().save(this);
	}

	public void delete() {
		Bot.getDatastore().delete(this);
	}

}
