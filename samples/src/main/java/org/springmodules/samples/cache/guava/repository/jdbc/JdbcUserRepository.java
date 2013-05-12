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
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.stereotype.Repository;
import org.springmodules.samples.cache.guava.domain.User;
import org.springmodules.samples.cache.guava.repository.UserRepository;

import javax.sql.DataSource;
import java.util.Collection;

/**
 * @author Omar Irbouh
 * @since 1.0.0
 */
@Repository
public class JdbcUserRepository extends NamedParameterJdbcDaoSupport implements UserRepository {

	final RowMapper<User> userMapper = BeanPropertyRowMapper.newInstance(User.class);

	@Autowired
	public JdbcUserRepository(DataSource dataSource) {
		super.setDataSource(dataSource);
	}

	@Override
	public Collection<User> findAll() {
		return getNamedParameterJdbcTemplate()
				.query(
						"select * from users",
						userMapper
				);
	}

	@Override
	public User findByUserName(String userName) {
		return getNamedParameterJdbcTemplate()
				.queryForObject(
						"select * from users where user_name = :user_name",
						new MapSqlParameterSource("user_name", userName),
						userMapper
				);
	}

}