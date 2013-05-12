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

import com.google.common.base.Objects;
import org.fest.assertions.core.Condition;
import org.springmodules.samples.cache.guava.domain.Post;
import org.springmodules.samples.cache.guava.domain.User;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Collection of {@link Condition}s.
 * @author Omar Irbouh
 * @since 1.0.0
 */
public final class SampleConditions {

	private SampleConditions() {
	}

	/**
	 * {@link Condition} that validates two users have the same values for all attributes
	 */
	public static Condition<User> sameAsUser(User user) {
		return new SameAsUserCondition(user);
	}

	private static class SameAsUserCondition extends Condition<User> {

		final User user;

		private SameAsUserCondition(User user) {
			this.user = checkNotNull(user);
		}

		@Override
		public boolean matches(User input) {
			return Objects.equal(user.getUserName(), input.getUserName()) &&
					Objects.equal(user.getFullName(), input.getFullName()) &&
					Objects.equal(user.getEmailAddress(), input.getEmailAddress());
		}
	}

	/**
	 * {@link Condition} that validates two posts have the same values for all attributes
	 */
	public static Condition<Post> sameAsPost(Post post) {
		return new SameAsPostCondition(post);
	}

	private static class SameAsPostCondition extends Condition<Post> {

		final Post post;

		private SameAsPostCondition(Post post) {
			this.post = checkNotNull(post);
		}

		@Override
		public boolean matches(Post input) {
			return Objects.equal(post.getUserName(), input.getUserName()) &&
					Objects.equal(post.getSubmitDate(), input.getSubmitDate()) &&
					Objects.equal(post.getContent(), input.getContent());
		}
	}

}