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

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * @author Omar Irbouh
 * @since 1.0
 */
public class GuavaCacheFactoryBeanTest {

	@Test
	public void testDefaultConfig() throws Exception {
		GuavaCacheFactoryBean factoryBean = new GuavaCacheFactoryBean();

		assertThat(factoryBean.isSingleton()).isTrue();
		assertThat(GuavaCache.class.equals(factoryBean.getObjectType())).isTrue();

		factoryBean.afterPropertiesSet();
		GuavaCache cache = factoryBean.getObject();

		assertThat(cache.getName()).isEqualTo("");
		assertThat(cache.isAllowNullValues()).isTrue();
	}

	@Test
	public void testCustomConfig() throws Exception {
		GuavaCacheFactoryBean factoryBean = new GuavaCacheFactoryBean();
		factoryBean.setBeanName("cacheName");
		factoryBean.setAllowNullValues(true);
		factoryBean.setSpec("maximumSize=2");
		factoryBean.afterPropertiesSet();
		GuavaCache cache = factoryBean.getObject();

		assertThat(cache.getName()).isEqualTo("cacheName");
		assertThat(cache.isAllowNullValues()).isTrue();

		// spec
		cache.put("key1", "value1");
		cache.put("key2", "value2");
		cache.put("key3", "value3");
		assertThat(cache.getNativeCache().size()).isEqualTo(2);

		// allow null
		cache.put("key", null);
		assertThat(cache.get("key").get()).isNull();
	}

	@Test
	public void testSingleton() throws Exception {
		GuavaCacheFactoryBean factoryBean = new GuavaCacheFactoryBean();
		factoryBean.afterPropertiesSet();
		GuavaCache cache1 = factoryBean.getObject();
		GuavaCache cache2 = factoryBean.getObject();

		assertThat(factoryBean.isSingleton()).isTrue();
		assertThat(cache1).isSameAs(cache2);
	}

}