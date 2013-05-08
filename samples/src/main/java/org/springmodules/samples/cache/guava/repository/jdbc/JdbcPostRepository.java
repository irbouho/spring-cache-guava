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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springmodules.samples.cache.guava.domain.Post;
import org.springmodules.samples.cache.guava.repository.PostRepository;

import javax.sql.DataSource;
import java.util.Collection;

/**
 * @author Omar Irbouh
 * @since 1.0.0
 */
@Repository
public class JdbcPostRepository extends NamedParameterJdbcDaoSupport implements PostRepository {

	final RowMapper<Post> postMapper = BeanPropertyRowMapper.newInstance(Post.class);
	final SimpleJdbcInsert insertPost;

	@Autowired
	public JdbcPostRepository(DataSource dataSource) {
		setDataSource(dataSource);

		insertPost = new SimpleJdbcInsert(dataSource)
				.withTableName("posts")
				.usingColumns("user_name", "submit_date", "content")
				.usingGeneratedKeyColumns("id");
	}

	@Override
	public void create(Post post) {
		Number id = insertPost.executeAndReturnKey(
				new MapSqlParameterSource()
						.addValue("user_name", post.getUserName())
						.addValue("submit_date", post.getSubmitDate())
						.addValue("content", post.getContent())
		);

		post.setId(id.intValue());
	}

	@Override
	public void update(Post post) {
		getNamedParameterJdbcTemplate()
				.update(
						"update posts set content = :content where id = :id",
						new BeanPropertySqlParameterSource(post)
				);
	}

	@Override
	public void delete(String userName, int id) {
		getNamedParameterJdbcTemplate()
				.update(
						"delete from posts where user_name = :user_name and id = :id",
						new MapSqlParameterSource()
								.addValue("user_name", userName)
								.addValue("id", id)
				);
	}

	@Override
	public Collection<Post> findByUserName(String userName) {
		return getNamedParameterJdbcTemplate()
				.query(
						"select * from posts where user_name = :user_name order by submit_date desc",
						new MapSqlParameterSource("user_name", userName),
						postMapper
				);
	}

}