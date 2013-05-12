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
package org.springmodules.samples.cache.guava.service.impl;

import com.google.common.collect.ImmutableList;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springmodules.samples.cache.guava.domain.Post;
import org.springmodules.samples.cache.guava.domain.User;
import org.springmodules.samples.cache.guava.repository.PostRepository;
import org.springmodules.samples.cache.guava.repository.UserRepository;

import java.util.Collection;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springmodules.samples.cache.guava.util.SampleTests.newPost;
import static org.springmodules.samples.cache.guava.util.SampleTests.newUser;

/**
 * @author Omar Irbouh
 * @since 1.0.0
 */
@RunWith(MockitoJUnitRunner.class)
public class SocialServiceImplTest {

	@Mock
	UserRepository userRepository;

	@Mock
	PostRepository postRepository;

	SocialServiceImpl socialService;

	@Before
	public void setUp() {
		socialService = new SocialServiceImpl(userRepository, postRepository);
	}

	@Test
	public void testFindAllUsers() {
		Collection<User> users = ImmutableList.of(
				newUser("taha", "Taha Irbouh", "taha@irbouh.net"),
				newUser("adam", "Adam Irbouh", "adam@irbouh.net")
		);

		given(userRepository.findAll()).willReturn(users);

		Collection<User> result = socialService.findAllUsers();

		assertThat(users).isSameAs(result);
		verify(userRepository).findAll();
	}

	@Test
	public void testFindUserName() {
		final String userName = "taha";
		User user = newUser(userName, "Taha Irbouh", "taha@irbouh.net");

		given(userRepository.findByUserName(userName)).willReturn(user);

		assertThat(socialService.findUserByUserName(userName)).isSameAs(user);
		verify(userRepository).findByUserName(userName);
	}

	@Test
	public void testFindPostsByUserName() {
		final String userName = "taha";

		Collection<Post> posts = ImmutableList.of(
				newPost(userName, "post - 1"),
				newPost(userName, "post - 2")
		);

		given(postRepository.findByUserName(userName)).willReturn(posts);

		Collection<Post> result = socialService.findPostsByUserName(userName);

		assertThat(posts).isSameAs(result);
		verify(postRepository).findByUserName(userName);
	}

	@Test
	public void testCreatePost() {
		final Post post = newPost("taha", "post - 1");

		socialService.createPost(post);

		verify(postRepository).create(post);
	}

	@Test(expected = NullPointerException.class)
	public void testCreateNullPost() {
		socialService.createPost(null);
	}

	@Test
	public void testUpdatePost() {
		final Post post = newPost("taha", "post - 1");

		socialService.updatePost(post);

		verify(postRepository).update(post);
	}

	@Test(expected = NullPointerException.class)
	public void testUpdateNullPost() {
		socialService.updatePost(null);
	}

	@Test
	public void testDeletePost() {
		final String userName = "taha";
		final int id = 1;

		socialService.deletePost(userName, id);

		verify(postRepository).delete(userName, id);
	}

	@Test(expected = NullPointerException.class)
	public void testDeleteNullPost() {
		socialService.deletePost(null, 1);
	}

}