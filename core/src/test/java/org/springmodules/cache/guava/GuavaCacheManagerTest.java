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

import com.google.common.collect.ImmutableList;
import com.google.common.util.concurrent.Uninterruptibles;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * @author Omar Irbouh
 * @since 1.0
 */
@RunWith(MockitoJUnitRunner.class)
public class GuavaCacheManagerTest {

	@Test
	public void testGetNamesStaticCaches() {
		Collection<GuavaCache> caches = ImmutableList.of(
				new GuavaCache("cache1"),
				new GuavaCache("cache2"),
				new GuavaCache("cache3")
		);

		GuavaCacheManager manager = new GuavaCacheManager();
		manager.setCaches(caches);
		manager.afterPropertiesSet();

		Collection<String> cacheNames = manager.getCacheNames();
		assertEquals(caches.size(), cacheNames.size());
		for (GuavaCache cache : caches) {
			assertTrue(cacheNames.contains(cache.getName()));
		}
	}

	@Test
	public void testGetCacheStaticCaches() {
		Collection<GuavaCache> caches = ImmutableList.of(
				new GuavaCache("cache1"),
				new GuavaCache("cache2"),
				new GuavaCache("cache3")
		);

		GuavaCacheManager manager = new GuavaCacheManager();
		manager.setCaches(caches);
		manager.afterPropertiesSet();

		for (GuavaCache cache : caches) {
			assertSame(cache, manager.getCache(cache.getName()));
		}
	}

	@Test
	public void testGetNamesDynamicCaches() {
		GuavaCacheManager manager = new GuavaCacheManager();
		manager.afterPropertiesSet();

		// no cache available by default
		assertEquals(0, manager.getCacheNames().size());

		// getting a new cache will add-it to available caches
		assertEquals("cache1", manager.getCache("cache1").getName());
		assertEquals(1, manager.getCacheNames().size());
		assertEquals("cache2", manager.getCache("cache2").getName());
		assertEquals(2, manager.getCacheNames().size());

		// get existing cache
		assertEquals("cache1", manager.getCache("cache1").getName());
		assertEquals(2, manager.getCacheNames().size());
	}

	@Test
	public void testGetCacheDynamicCachesDefaultConfig() {
		GuavaCacheManager manager = new GuavaCacheManager();
		manager.afterPropertiesSet();

		GuavaCache cache = (GuavaCache) manager.getCache("cache1");
		assertTrue(cache.isAllowNullValues());
	}

	@Test
	public void testGetCacheDynamicCachesCustomConfig() {
		GuavaCacheManager manager = new GuavaCacheManager();
		manager.setAllowNullValues(false);
		manager.setSpec("maximumSize=2,expireAfterWrite=2s");
		manager.afterPropertiesSet();

		GuavaCache cache = (GuavaCache) manager.getCache("cache1");
		assertFalse(cache.isAllowNullValues());
		cache.put("key1", "value1");
		cache.put("key2", "value2");
		cache.put("key3", "value3");
		assertEquals(2, cache.getNativeCache().size());

		Uninterruptibles.sleepUninterruptibly(3, TimeUnit.SECONDS);
		// evict stale entries
		cache.getNativeCache().cleanUp();
		assertEquals(0, cache.getNativeCache().size());
	}

}