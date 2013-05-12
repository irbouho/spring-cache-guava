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

import com.google.common.collect.Maps;
import org.junit.Test;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springmodules.samples.cache.guava.domain.User;

import java.util.Collection;
import java.util.Map;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.springmodules.samples.cache.guava.util.SampleConditions.sameAsUser;
import static org.springmodules.samples.cache.guava.util.SampleFunctions.userName;

/**
 * @author Omar Irbouh
 * @since 1.0.0
 */
public class JdbcUserRepositoryTest extends AbstractJdbcRepositoryTest {

	@Test
	public void testFindAll() {
		// load users from db
		Map<String, User> resultMap = Maps.uniqueIndex(userRepository.findAll(), userName());

		assertThat(resultMap).hasSameSizeAs(userMap.values());

		// verify
		for (User user : userMap.values()) {
			assertThat(user).is(sameAsUser(resultMap.get(user.getUserName())));
		}
	}

	@Test
	public void testFindAll_Empty() {
		// delete all data
		helper.deleteFromTables("posts", "users");

		// load users from db
		Collection<User> users = userRepository.findAll();

		// verify
		assertThat(users).isEmpty();
	}

	@Test
	public void testFindByUserName() {
		User expected = userMap.values().iterator().next();

		// load user from db
		User user = userRepository.findByUserName(expected.getUserName());

		// verify
		assertThat(user).is(sameAsUser(expected));
	}

	@Test(expected = EmptyResultDataAccessException.class)
	public void testFindByUserName_NotFound() {
		final String userName = "|_x_|";

		// load user from db
		userRepository.findByUserName(userName);
	}

}