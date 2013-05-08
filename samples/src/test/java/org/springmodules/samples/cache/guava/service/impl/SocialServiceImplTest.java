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
import org.mockito.exceptions.verification.ArgumentsAreDifferent;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springmodules.samples.cache.guava.domain.Post;
import org.springmodules.samples.cache.guava.domain.User;
import org.springmodules.samples.cache.guava.repository.PostRepository;
import org.springmodules.samples.cache.guava.repository.UserRepository;

import java.util.Collection;
import java.util.Date;

import static org.junit.Assert.assertSame;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

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

		assertSame(users, result);
		verify(userRepository).findAll();
	}

	@Test
	public void testFindUserName() {
		final String userName = "taha";
		User user = newUser(userName, "Taha Irbouh", "taha@irbouh.net");

		given(userRepository.findByUserName(userName)).willReturn(user);

		assertSame(user, socialService.findUserByUserName(userName));
		verify(userRepository).findByUserName(userName);
	}

	@Test
	public void testFindPostsByUserName() {
		final String userName = "taha";

		Collection<Post> posts = ImmutableList.of(
				newPost(1, userName, "post - 1"),
				newPost(2, userName, "post - 2")
		);

		given(postRepository.findByUserName(userName)).willReturn(posts);

		Collection<Post> result = socialService.findPostsByUserName(userName);

		assertSame(posts, result);
		verify(postRepository).findByUserName(userName);
	}

	@Test
	public void testCreatePost() {
		final Post post = newPost(0, "taha", "post - 1");

		socialService.createPost(post);

		verify(postRepository).create(post);
	}

	@Test(expected = NullPointerException.class)
	public void testCreateNullPost() {
		socialService.createPost(null);
	}

	@Test
	public void testUpdatePost() {
		final Post post = newPost(1, "taha", "post - 1");

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

	User newUser(String userName, String fullName, String emailAddress) {
		User user = new User();
		user.setUserName(userName);
		user.setFullName(fullName);
		user.setEmailAddress(emailAddress);

		return user;
	}

	Post newPost(int id, String userName, String content) {
		Post post = new Post();
		post.setId(id);
		post.setUserName(userName);
		post.setContent(content);
		post.setSubmitDate(new Date());

		return post;
	}

	@SuppressWarnings("unused")
	static class PostMatchingAnswer implements Answer<Void> {

		final Post expected;

		PostMatchingAnswer(Post expected) {
			this.expected = expected;
		}

		@Override
		public Void answer(InvocationOnMock invocation) throws Throwable {
			Post post = (Post) invocation.getArguments()[0];
			if (!this.expected.equals(post)) {
				throw new ArgumentsAreDifferent(
						String.format("Post %s is different from expected %s", post, expected));
			}

			return null;
		}
	}

}