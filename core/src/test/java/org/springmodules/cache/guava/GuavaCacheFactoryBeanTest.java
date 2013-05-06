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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * @author Omar Irbouh
 * @since 1.0
 */
@RunWith(MockitoJUnitRunner.class)
public class GuavaCacheFactoryBeanTest {

	@Test
	public void testDefaultConfig()throws Exception {
		GuavaCacheFactoryBean factoryBean = new GuavaCacheFactoryBean();

		assertTrue(factoryBean.isSingleton());
		assertTrue(GuavaCache.class.equals(factoryBean.getObjectType()));

		factoryBean.afterPropertiesSet();
		GuavaCache cache = factoryBean.getObject();

		assertEquals("", cache.getName());
		assertTrue(cache.isAllowNullValues());
	}

	@Test
	public void testCustomConfig()throws Exception {
		GuavaCacheFactoryBean factoryBean = new GuavaCacheFactoryBean();
		factoryBean.setBeanName("cacheName");
		factoryBean.setAllowNullValues(true);
		factoryBean.setSpec("maximumSize=2");
		factoryBean.afterPropertiesSet();
		GuavaCache cache = factoryBean.getObject();

		assertEquals("cacheName", cache.getName());
		assertTrue(cache.isAllowNullValues());

		// spec
		cache.put("key1", "value1");
		cache.put("key2", "value2");
		cache.put("key3", "value3");
		assertEquals(2, cache.getNativeCache().size());

		// allow null
		cache.put("key", null);
		assertNull(cache.get("key").get());

	}

	@Test
	public void testSingleton()throws Exception {
		GuavaCacheFactoryBean factoryBean = new GuavaCacheFactoryBean();
		factoryBean.afterPropertiesSet();
		GuavaCache cache1 = factoryBean.getObject();
		GuavaCache cache2 = factoryBean.getObject();

		assertTrue(factoryBean.isSingleton());
		assertSame(cache1, cache2);
	}

}