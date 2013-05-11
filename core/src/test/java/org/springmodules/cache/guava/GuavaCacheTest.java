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
package org.springmodules.cache.guava;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheBuilderSpec;
import org.junit.Test;
import org.springframework.cache.Cache;

import java.util.concurrent.TimeUnit;

import static com.google.common.util.concurrent.Uninterruptibles.sleepUninterruptibly;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

/**
 * @author Omar Irbouh
 * @since 1.0
 */
public class GuavaCacheTest {

	@Test(expected = NullPointerException.class)
	public void testNameIsRequired() {
		new GuavaCache(null);
	}

	@Test
	public void testNewWithSpec() {
		CacheBuilderSpec spec = CacheBuilderSpec.parse("maximumSize=2");
		GuavaCache cache = new GuavaCache("name", spec, true);
		cache.put("key1", "value1");
		cache.put("key2", "value2");
		cache.put("key3", "value3");

		assertThat(cache.getNativeCache().size()).isEqualTo(2);
	}

	@Test
	public void testGet() {
		GuavaCache cache = new GuavaCache("name");
		cache.getNativeCache().put("key", "value");

		assertThat(cache.get("key").get()).isEqualTo("value");
	}

	@Test
	public void testGetAbsent() {
		GuavaCache cache = new GuavaCache("name");

		assertThat(cache.get("key")).isNull();
	}

	@Test
	public void testPut() {
		GuavaCache cache = new GuavaCache("name");
		cache.put("key", "value");

		assertThat(cache.getNativeCache().getIfPresent("key"))
				.isNotNull()
				.isEqualTo("value");
	}

	@Test
	public void testEvict() {
		GuavaCache cache = new GuavaCache("name");
		cache.getNativeCache().put("key", "value");

		assertThat(cache.getNativeCache().getIfPresent("key"))
				.isNotNull()
				.isEqualTo("value");

		cache.evict("key");
		assertThat(cache.getNativeCache().getIfPresent("key")).isNull();
	}

	@Test
	public void testClear() {
		GuavaCache cache = new GuavaCache("name");
		cache.getNativeCache().put("key1", "value1");
		cache.getNativeCache().put("key2", "value2");
		cache.getNativeCache().put("key3", "value3");

		assertThat(cache.getNativeCache().size()).isEqualTo(3);

		cache.clear();
		assertThat(cache.getNativeCache().size()).isZero();
	}

	@Test
	public void testAllowNullValues() {
		Cache cache = new GuavaCache("name", true);
		cache.put("key", null);

		assertThat(cache.get("key").get()).isNull();
	}

	@Test(expected = NullPointerException.class)
	public void testDisallowNullValues() {
		Cache cache = new GuavaCache("name", false);

		cache.put("key", null);
	}

	@Test
	public void testExpire() {
		CacheBuilder<Object, Object> builder = CacheBuilder.newBuilder().expireAfterWrite(2, TimeUnit.SECONDS);
		GuavaCache cache = new GuavaCache("name", builder, false);
		cache.getNativeCache().put("key", "value");
		assertEquals("value", cache.get("key").get());

		// wait for expiration
		sleepUninterruptibly(3, TimeUnit.SECONDS);

		assertThat(cache.get("key")).isNull();
	}

}