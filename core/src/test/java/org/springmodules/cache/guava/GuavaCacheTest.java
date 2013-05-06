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

import com.google.common.cache.CacheBuilderSpec;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.cache.Cache;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author Omar Irbouh
 * @since 1.0
 */
@RunWith(MockitoJUnitRunner.class)
public class GuavaCacheTest {

	@Test(expected = NullPointerException.class)
	public void nameIsRequired() {
		new GuavaCache(null);
	}

	@Test
	public void newWithSpec() {
		CacheBuilderSpec spec = CacheBuilderSpec.parse("maximumSize=2");
		GuavaCache cache = new GuavaCache("name", spec, true);
		cache.put("key1", "value1");
		cache.put("key2", "value2");
		cache.put("key3", "value3");
		assertEquals(2, cache.getNativeCache().size());
	}

	@Test
	public void get() {
		GuavaCache cache = new GuavaCache("name");
		cache.getNativeCache().put("key", "value");
		assertEquals("value", cache.get("key").get());
	}

	@Test
	public void getAbsent() {
		GuavaCache cache = new GuavaCache("name");
		assertNull(cache.get("key"));
	}

	@Test
	public void put() {
		GuavaCache cache = new GuavaCache("name");
		cache.put("key", "value");
		assertEquals("value", cache.getNativeCache().getIfPresent("key"));
	}

	@Test
	public void evict() {
		GuavaCache cache = new GuavaCache("name");
		cache.getNativeCache().put("key", "value");

		assertEquals("value", cache.getNativeCache().getIfPresent("key"));
		cache.evict("key");
		assertNull(cache.getNativeCache().getIfPresent("key"));
	}

	@Test
	public void clear() {
		GuavaCache cache = new GuavaCache("name");
		cache.getNativeCache().put("key1", "value1");
		cache.getNativeCache().put("key2", "value2");
		cache.getNativeCache().put("key3", "value3");

		assertEquals(3, cache.getNativeCache().size());
		cache.clear();
		assertEquals(0, cache.getNativeCache().size());
	}

	@Test
	public void allowNullValues() {
		Cache cache = new GuavaCache("name", true);
		cache.put("key", null);
		Cache.ValueWrapper value = cache.get("key");
		assertNull(value.get());
	}

	@Test(expected = NullPointerException.class)
	public void disallowNullValues() {
		Cache cache = new GuavaCache("name", false);
		cache.put("key", null);
	}

}