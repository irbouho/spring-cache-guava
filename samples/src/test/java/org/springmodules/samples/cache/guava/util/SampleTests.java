/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springmodules.samples.cache.guava.util;

import com.google.common.collect.Maps;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springmodules.samples.cache.guava.domain.Post;
import org.springmodules.samples.cache.guava.domain.User;

import javax.sql.DataSource;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.springmodules.samples.cache.guava.util.SampleFunctions.postId;

/**
 * Helper class used for testing.
 * @author Omar Irbouh
 * @since 1.0.0
 */
public final class SampleTests {

	private SampleTests() {
	}

	/**
	 * Helper method for creating a {@link User}.
	 */
	public static User newUser(String userName, String fullName, String emailAddress) {
		User user = new User();
		user.setUserName(userName);
		user.setFullName(fullName);
		user.setEmailAddress(emailAddress);

		return user;
	}

	/**
	 * Helper method for creating a {@link Post}.
	 */
	public static Post newPost(String userName, String content) {
		Post post = new Post();
		post.setUserName(userName);
		post.setContent(content);
		post.setSubmitDate(new Date());

		return post;
	}

	/**
	 * Helper method for creating a {@link Post}.
	 */
	public static Post newPostWithId(int id, String userName, String content) {
		Post post = newPost(userName, content);
		post.setId(id);

		return post;
	}

	/**
	 * builder method for creating a new helper instance for a given {@link DataSource}
	 */
	public static JdbcHelper forDataSource(DataSource dataSource) {
		return new JdbcHelper(dataSource);
	}

	/**
	 * Helper class used for executing JDBC statements.
	 */
	public static final class JdbcHelper {

		private final NamedParameterJdbcOperations jdbcTemplate;
		private final SimpleJdbcInsert insertPost;

		private final RowMapper<Post> postMapper = BeanPropertyRowMapper.newInstance(Post.class);

		/**
		 * private constructor
		 * @see {@link SampleTests#forDataSource(javax.sql.DataSource)}
		 */
		private JdbcHelper(DataSource dataSource) {
			this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
			this.insertPost = new SimpleJdbcInsert(dataSource)
					.withTableName("posts")
					.usingColumns("user_name", "submit_date", "content")
					.usingGeneratedKeyColumns("id");
		}

		public JdbcHelper deleteFromTables(String... tableNames) {
			for (String tableName : tableNames) {
				this.jdbcTemplate.getJdbcOperations()
						.update("delete from " + tableName);
			}

			return this;
		}

		public JdbcHelper createUsers(Iterable<User> users) {
			for (User user : users) {
				createUser(user);
			}

			return this;
		}

		public JdbcHelper createUser(User user) {
			this.jdbcTemplate
					.update("insert into users (user_name, full_name, email_address)" +
							" values (:userName, :fullName, :emailAddress)",
							new BeanPropertySqlParameterSource(user));

			return this;
		}

		public JdbcHelper createPosts(Iterable<Post> posts) {
			for (Post post : posts) {
				createPost(post);
			}

			return this;
		}

		public JdbcHelper createPost(Post post) {
			Number id = insertPost.executeAndReturnKey(
					new MapSqlParameterSource()
							.addValue("user_name", post.getUserName())
							.addValue("submit_date", post.getSubmitDate())
							.addValue("content", post.getContent())
			);

			post.setId(id.intValue());

			return this;
		}

		public Map<Integer, Post> loadAllPosts() {
			List<Post> posts = this.jdbcTemplate
					.query("select * from posts order by submit_date desc",
							postMapper);

			return Maps.uniqueIndex(posts, postId());
		}
	}

}