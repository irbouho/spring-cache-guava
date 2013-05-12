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
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springmodules.cache.guava.GuavaCache;
import org.springmodules.cache.guava.GuavaCacheManager;
import org.springmodules.samples.cache.guava.domain.Post;
import org.springmodules.samples.cache.guava.domain.User;
import org.springmodules.samples.cache.guava.repository.PostRepository;
import org.springmodules.samples.cache.guava.repository.UserRepository;
import org.springmodules.samples.cache.guava.service.SocialService;

import java.util.Collection;

import static com.google.common.collect.Iterables.limit;
import static com.google.common.collect.Lists.newArrayList;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springmodules.samples.cache.guava.util.SampleTests.newPost;
import static org.springmodules.samples.cache.guava.util.SampleTests.newPostWithId;
import static org.springmodules.samples.cache.guava.util.SampleTests.newUser;

/**
 * @author Omar Irbouh
 * @since 1.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SocialServiceImplCacheTest.AppConfig.class})
public class SocialServiceImplCacheTest {

	@Autowired
	GuavaCache usersCache;

	@Autowired
	GuavaCache postsCache;

	@Autowired
	UserRepository userRepository;

	@Autowired
	PostRepository postRepository;

	@Autowired
	SocialService socialService;

	@Before
	public void setUp() {
		// flush caches
		usersCache.clear();
		postsCache.clear();
	}

	@After
	public void tearDown() {
		// reset container injected mocks
		Mockito.reset(userRepository, postRepository);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testFindAllUsers() {
		final Collection<User> users = ImmutableList.of(
				newUser("taha", "Taha Irbouh", "taha@irbouh.net"),
				newUser("adam", "Adam Irbouh", "adam@irbouh.net")
		);

		// return a clone of the users list
		when(userRepository.findAll()).thenReturn(newArrayList(users));

		// call the service method few times
		assertThat(socialService.findAllUsers()).containsAll(users);
		assertThat(socialService.findAllUsers()).containsAll(users);

		// verify data is in cache
		assertThat(users).containsAll((Iterable<? extends User>) usersCache.get("all-users").get());

		// verify number of times repository was invoked
		verify(userRepository, times(1)).findAll();
	}

	@Test
	public void testFindUserByUserName() {
		final String userName = "taha";
		final User user = newUser(userName, "Taha Irbouh", "taha@irbouh.net");

		when(userRepository.findByUserName(userName)).thenReturn(user);

		// call the service method few times
		assertThat(socialService.findUserByUserName(userName)).isSameAs(user);
		assertThat(socialService.findUserByUserName(userName)).isSameAs(user);

		// verify data is in cache
		assertThat(usersCache.get(userName).get()).isSameAs(user);

		// verify number of times repository was invoked
		verify(userRepository, times(1)).findByUserName(userName);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testFindPostsByUserName() {
		final String userName = "taha";
		final Collection<Post> posts = newArrayList(
				newPostWithId(1, userName, "post 1"),
				newPostWithId(2, userName, "post 2"),
				newPostWithId(3, userName, "post 3")
		);

		// return a clone of the posts
		when(postRepository.findByUserName(userName)).thenReturn(newArrayList(posts));

		// call the service method few times
		assertThat(socialService.findPostsByUserName(userName)).containsAll(posts);
		assertThat(socialService.findPostsByUserName(userName)).containsAll(posts);

		// verify data is in cache
		assertThat(posts).containsAll((Iterable<? extends Post>) postsCache.get(userName).get());

		// verify number of times repository was invoked
		verify(postRepository, times(1)).findByUserName(userName);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testCreatePost() {
		final String userName = "taha";
		final Collection<Post> posts = newArrayList(
				newPostWithId(1, userName, "post 1"),
				newPostWithId(2, userName, "post 2"),
				newPostWithId(3, userName, "post 3")
		);

		final Post newPost = newPost(userName, "post 4");

		final Collection<Post> postsAfterAdd = newArrayList();
		postsAfterAdd.addAll(posts);
		postsAfterAdd.add(newPost);

		// return a clone of the posts
		when(postRepository.findByUserName(userName))
				.thenReturn(newArrayList(posts))
				.thenReturn(newArrayList(postsAfterAdd));

		doAnswer(new Answer() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				// set new post id
				Post post = (Post) invocation.getArguments()[0];
				post.setId(posts.size() + 1);

				// stubbed method is void
				return null;
			}
		}).when(postRepository).create(newPost);

		// load user posts
		assertThat(socialService.findPostsByUserName(userName)).containsAll(posts);

		// verify data is in cache
		assertThat(posts).containsAll((Iterable<? extends Post>) postsCache.get(userName).get());

		// create new post
		socialService.createPost(newPost);

		// verify cache was flushed
		assertThat(postsCache.getNativeCache().size()).isZero();

		// reload user posts
		assertThat(socialService.findPostsByUserName(userName))
				.hasSameSizeAs(postsAfterAdd)
				.containsAll(postsAfterAdd)
				.contains(newPost);

		// verify number of times repository was invoked
		verify(postRepository, times(1)).create(newPost);
	}

	@Test
	public void testUpdatePost() {
		final String userName = "taha";
		final Collection<Post> posts = newArrayList(
				newPostWithId(1, userName, "post 1"),
				newPostWithId(2, userName, "post 2"),
				newPostWithId(3, userName, "post 3")
		);

		final Post updatePost = newPostWithId(3, userName, "post 3 - new content");

		final Collection<Post> postsAfterUpdate = newArrayList(limit(posts, 2));
		postsAfterUpdate.add(updatePost);

		when(postRepository.findByUserName(userName)).thenReturn(posts);

		// load user posts
		assertThat(socialService.findPostsByUserName(userName)).isSameAs(posts);

		// verify data is in cache
		assertThat(postsCache.get(userName).get()).isSameAs(posts);

		// update post
		socialService.updatePost(updatePost);

		// verify cache was flushed
		assertThat(postsCache.getNativeCache().size()).isZero();

		// reload user posts
		assertThat(socialService.findPostsByUserName(userName))
				.hasSameSizeAs(postsAfterUpdate)
				.containsAll(postsAfterUpdate)
				.contains(updatePost);

		// verify number of times repository was invoked
		verify(postRepository, times(1)).update(updatePost);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testDeletePost() {
		final String userName = "taha";
		final int postId = 3;
		final Collection<Post> posts = newArrayList(
				newPostWithId(1, userName, "post 1"),
				newPostWithId(2, userName, "post 2"),
				newPostWithId(3, userName, "post 3")
		);

		final Collection<Post> postsAfterDelete = newArrayList(limit(posts, 2));

		// return a clone of the posts
		when(postRepository.findByUserName(userName))
				.thenReturn(newArrayList(posts))
				.thenReturn(newArrayList(postsAfterDelete));

		// load user posts
		assertThat(socialService.findPostsByUserName(userName)).containsAll(posts);

		// verify data is in cache
		assertThat(posts).containsAll((Iterable<? extends Post>) postsCache.get(userName).get());

		// create new post
		socialService.deletePost(userName, postId);

		// verify cache was flushed
		assertThat(postsCache.getNativeCache().size()).isZero();

		// reload user posts
		assertThat(socialService.findPostsByUserName(userName))
				.hasSameSizeAs(postsAfterDelete)
				.containsAll(postsAfterDelete);

		// verify number of times repository was invoked
		verify(postRepository, times(1)).delete(userName, postId);
	}

	@Configuration
	@EnableCaching
	public static class AppConfig {

		@Bean
		public SocialService socialService() {
			return new SocialServiceImpl(userRepository(), postRepository());
		}

		@Bean
		public UserRepository userRepository() {
			return mock(UserRepository.class);
		}

		@Bean
		public PostRepository postRepository() {
			return mock(PostRepository.class);
		}

		@Bean
		public CacheManager cacheManager() {
			GuavaCacheManager manager = new GuavaCacheManager();
			manager.setCaches(ImmutableList.of(usersCache(), postsCache()));
			return manager;
		}

		@Bean
		public GuavaCache usersCache() {
			return new GuavaCache("users-cache");
		}

		@Bean
		public GuavaCache postsCache() {
			return new GuavaCache("posts-cache");
		}

	}

}