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
package org.springmodules.samples.cache.guava.repository.jdbc;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimaps;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springmodules.samples.cache.guava.domain.Post;
import org.springmodules.samples.cache.guava.domain.User;
import org.springmodules.samples.cache.guava.repository.PostRepository;
import org.springmodules.samples.cache.guava.repository.UserRepository;
import org.springmodules.samples.cache.guava.util.SampleTests;

import javax.sql.DataSource;
import java.util.Collection;

import static java.util.Arrays.asList;
import static org.springmodules.samples.cache.guava.util.SampleFunctions.postUserName;
import static org.springmodules.samples.cache.guava.util.SampleFunctions.userName;
import static org.springmodules.samples.cache.guava.util.SampleTests.newPost;
import static org.springmodules.samples.cache.guava.util.SampleTests.newUser;

/**
 * Base JDBC integration tests class.
 * @author Omar Irbouh
 * @since 1.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:/META-INF/config/applicationContext-config.xml"})
@TransactionConfiguration(defaultRollback = true)
@Transactional
public abstract class AbstractJdbcRepositoryTest {

	@Autowired
	protected UserRepository userRepository;

	@Autowired
	protected PostRepository postRepository;

	protected SampleTests.JdbcHelper helper;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.helper = SampleTests.forDataSource(dataSource);
	}

	protected ImmutableMap<String, User> userMap;
	protected ImmutableMultimap<String, Post> postMap;

	/**
	 * setup test data:
	 * <ul>
	 * <li>user-1 has 2 posts</li>
	 * <li>user-2 has 1 post</li>
	 * <li>user-3 has 0 posts</li>
	 * </ul>
	 */
	@Before
	public void setUp() {
		// delete data from tables
		helper.deleteFromTables("posts", "users");

		// create test users
		Collection<User> users = asList(
				newUser("user-1", "User name 1", "user1@users.com"),
				newUser("user-2", "User name 2", "user2@users.com"),
				newUser("user-3", "User name 3", "user3@users.com")
		);

		// create test posts
		Collection<Post> posts = asList(
				newPost("user-1", "content 11"),
				newPost("user-1", "content 12"),
				newPost("user-2", "content 21")
		);

		// persist into data store
		helper.createUsers(users)
				.createPosts(posts);

		// store for later use
		userMap = Maps.uniqueIndex(users, userName());
		postMap = Multimaps.index(posts, postUserName());
	}

}