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
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.transaction.AbstractTransactionSupportingCacheManager;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Collections;

/**
 * {@link CacheManager} implementation backed by {@link GuavaCache}.
 * @author Omar Irbouh
 * @since 1.0
 */
public class GuavaCacheManager extends AbstractTransactionSupportingCacheManager {

	private Collection<GuavaCache> caches;

	private String spec;

	private volatile CacheBuilder<Object, Object> cacheBuilder;

	private boolean allowNullValues = true;

	public GuavaCacheManager() {
	}

	public void setCaches(Collection<GuavaCache> caches) {
		this.caches = caches;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public String getSpec() {
		return spec;
	}

	public void setAllowNullValues(boolean allowNullValues) {
		this.allowNullValues = allowNullValues;
	}

	public boolean isAllowNullValues() {
		return allowNullValues;
	}

	@Override
	protected Collection<? extends Cache> loadCaches() {
		return (caches != null) ? caches : Collections.<GuavaCache>emptyList();
	}

	@Override
	public Cache getCache(String name) {
		Cache cache = super.getCache(name);
		if (cache == null) {
			// create a new cache
			cache = createGuavaCache(name);

			// add to collection of available caches
			addCache(cache);
		}
		return cache;
	}

	private GuavaCache createGuavaCache(String name) {
		// create GuavaCache
		return new GuavaCache(name, getCacheBuilder(), allowNullValues);
	}

	private CacheBuilder<Object, Object> getCacheBuilder() {
		if (cacheBuilder == null) {
			synchronized (this) {
				if (cacheBuilder == null) {
					if (StringUtils.hasText(spec)) {
						cacheBuilder = CacheBuilder.from(spec);
					}
					else {
						cacheBuilder = CacheBuilder.newBuilder();
					}
				}
				notify();
			}
		}

		return cacheBuilder;
	}

}