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

import com.google.common.base.Function;
import org.springmodules.samples.cache.guava.domain.Post;
import org.springmodules.samples.cache.guava.domain.User;

/**
 * Collection of {@link Function}s.
 * @author Omar Irbouh
 * @since 1.0.0
 */
public final class SampleFunctions {

	private SampleFunctions() {
	}

	/**
	 * {@link Function} that returns the username of a given {@link User}
	 */
	public static Function<User, String> userName() {
		return UserUserNameFunction.INSTANCE;
	}

	enum UserUserNameFunction implements Function<User, String> {
		INSTANCE;

		@Override
		public String apply(User input) {
			return input.getUserName();
		}
	}

	/**
	 * {@link Function} that returns the id of a given {@link Post}
	 */
	public static Function<Post, Integer> postId() {
		return PostIdFunction.INSTANCE;
	}

	enum PostIdFunction implements Function<Post, Integer> {
		INSTANCE;

		@Override
		public Integer apply(Post input) {
			return input.getId();
		}
	}

	/**
	 * {@link Function} that returns the username of a given {@link Post}
	 */
	public static Function<Post, String> postUserName() {
		return PostUserNameFunction.INSTANCE;
	}

	enum PostUserNameFunction implements Function<Post, String> {
		INSTANCE;

		@Override
		public String apply(Post input) {
			return input.getUserName();
		}
	}

}