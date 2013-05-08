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
import java.util.Date;

/**
 * @author Omar Irbouh
 * @since 1.0.0
 */
public class Post implements Serializable {

	private static final long serialVersionUID = 9117377389553127823L;

	private String userName;
	private int id;
	private Date submitDate;
	private String content;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getSubmitDate() {
		return submitDate;
	}

	public void setSubmitDate(Date submitDate) {
		this.submitDate = submitDate;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || !getClass().equals(o.getClass())) {
			return false;
		}

		Post other = (Post) o;
		return Objects.equal(this.userName, other.userName) &&
				Objects.equal(this.id, other.id);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(userName, id);
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this)
				.add("userName", userName)
				.add("id", id)
				.add("submitDate", submitDate)
				.add("content", content)
				.toString();
	}

}