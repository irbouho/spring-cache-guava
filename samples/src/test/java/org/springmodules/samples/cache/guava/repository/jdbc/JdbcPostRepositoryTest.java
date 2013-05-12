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

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.Iterables;
import org.junit.Test;
import org.springmodules.samples.cache.guava.domain.Post;

import java.util.Collection;
import java.util.Map;

import static com.google.common.collect.Iterables.getOnlyElement;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.springmodules.samples.cache.guava.util.SampleConditions.sameAsPost;
import static org.springmodules.samples.cache.guava.util.SampleTests.newPost;

/**
 * @author Omar Irbouh
 * @since 1.0.0
 */
public class JdbcPostRepositoryTest extends AbstractJdbcRepositoryTest {

	@Test
	public void testCreate() {
		// delete all existing posts
		helper.deleteFromTables("posts");

		String userName = "user-3";
		Post post31 = newPost(userName, "content-31");
		postRepository.create(post31);

		// verify it was saved (non-zero id assigned)
		assertThat(post31.getId()).isNotZero();

		// load all posts
		Map<Integer, Post> posts = helper.loadAllPosts();
		assertThat(posts).hasSize(1);

		assertThat(getOnlyElement(posts.values())).is(sameAsPost(post31));
	}

	@Test
	public void testUpdate() {
		// get first post
		Post post = Iterables.get(postMap.values(), 0);
		final int id = post.getId();

		// update in store
		final String content = "x-x--- new content ---x-x";
		post.setContent(content);
		postRepository.update(post);

		// verify
		Post updatedPost = helper.loadAllPosts().get(id);
		assertThat(updatedPost.getContent()).isEqualTo(content);
	}

	@Test
	public void testDelete() {
		// get first post
		Post post = Iterables.get(postMap.values(), 0);
		final int id = post.getId();

		postRepository.delete(post.getUserName(), post.getId());

		// verify
		Map<Integer, Post> posts = helper.loadAllPosts();
		assertThat(posts.keySet()).doesNotContain(id);
	}

	@Test
	public void findByUserName_HasPosts() {
		// user1 has posts
		final String userName = "user-1";

		ImmutableCollection<Post> userPosts = postMap.get(userName);
		assertThat(userPosts).isNotEmpty();

		// load from db
		Collection<Post> posts = postRepository.findByUserName(userName);

		// verify
		assertThat(posts).hasSameSizeAs(userPosts);
		assertThat(userPosts).containsAll(posts);
		assertThat(posts).containsAll(userPosts);
	}

	@Test
	public void findByUserName_NoPosts() {
		// user3 has 0 posts
		final String userName = "user-3";

		ImmutableCollection<Post> userPosts = postMap.get(userName);
		assertThat(userPosts).isEmpty();

		// load from db
		Collection<Post> posts = postRepository.findByUserName(userName);

		// verify
		assertThat(posts).isEmpty();
	}

}