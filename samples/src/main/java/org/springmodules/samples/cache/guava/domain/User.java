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
package org.springmodules.samples.cache.guava.domain;

import com.google.common.base.Objects;

import java.io.Serializable;

/**
 * @author Omar Irbouh
 * @since 1.0.0
 */
public class User implements Serializable {

	private static final long serialVersionUID = -926354811541833155L;

	private String userName;
	private String fullName;
	private String emailAddress;

	public User() {
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || !getClass().equals(o.getClass())) {
			return false;
		}

		User other = (User) o;
		return Objects.equal(this.userName, other.userName);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(userName);
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this)
				.add("userName", userName)
				.add("fullName", fullName)
				.add("email", emailAddress)
				.toString();
	}

}