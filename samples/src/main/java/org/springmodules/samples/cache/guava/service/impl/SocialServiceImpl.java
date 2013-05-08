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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springmodules.samples.cache.guava.domain.Post;
import org.springmodules.samples.cache.guava.domain.User;
import org.springmodules.samples.cache.guava.repository.PostRepository;
import org.springmodules.samples.cache.guava.repository.UserRepository;
import org.springmodules.samples.cache.guava.service.SocialService;

import java.util.Collection;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Omar Irbouh
 * @since 1.0.0
 */
@Service
@Transactional(readOnly = true)
public class SocialServiceImpl implements SocialService {

	final UserRepository userRepository;
	final PostRepository postRepository;

	@Autowired
	public SocialServiceImpl(UserRepository userRepository,
							 PostRepository postRepository) {
		this.userRepository = checkNotNull(userRepository);
		this.postRepository = checkNotNull(postRepository);
	}

	@Override
	public Collection<User> findAllUsers() {
		return userRepository.findAll();
	}

	@Override
	public User findUserByUserName(String userName) {
		return userRepository.findByUserName(checkNotNull(userName));
	}

	@Override
	public Collection<Post> findPostsByUserName(String userName) {
		return postRepository.findByUserName(checkNotNull(userName));
	}

	@Override
	@Transactional(readOnly = false)
	public void createPost(Post post) {
		postRepository.create(checkNotNull(post));
	}

	@Override
	@Transactional(readOnly = false)
	public void updatePost(Post post) {
		postRepository.update(checkNotNull(post));
	}

	@Override
	@Transactional(readOnly = false)
	public void deletePost(String userName, int id) {
		postRepository.delete(checkNotNull(userName), id);
	}

}