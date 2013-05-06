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
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;

import java.io.Serializable;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Simple {@link Cache} implementation backed by {@link com.google.common.cache.Cache}.
 * @author Omar Irbouh
 * @since 1.0
 */
public class GuavaCache implements Cache {

	private static final Object NULL_HOLDER = new NullHolder();

	private final String name;

	private final com.google.common.cache.Cache<Object, Object> store;

	private final boolean allowNullValues;

	/**
	 * Create a new GuavaCache with the specified name.
	 * @param name the name of the cache
	 */
	public GuavaCache(String name) {
		this(name, CacheBuilder.newBuilder(), true);
	}

	/**
	 * Create a new GuavaCache with the specified name.
	 * @param name the name of the cache
	 * @param allowNullValues whether to accept and convert null values for this cache
	 */
	public GuavaCache(String name, boolean allowNullValues) {
		this(name, CacheBuilder.newBuilder(), allowNullValues);
	}

	/**
	 * Create a new GuavaCache using the specified name and {@link CacheBuilderSpec specification}
	 * @param name the name of the cache
	 * @param spec the cache builder specification to use to build he cache
	 */
	public GuavaCache(String name, CacheBuilderSpec spec, boolean allowNullValues) {
		this(name, CacheBuilder.from(spec), allowNullValues);
	}

	/**
	 * Create a new GuavaCache using the specified name and {@link CacheBuilderSpec specification}
	 * @param name the name of the cache
	 * @param builder the cache builder to use to build the cache
	 */
	public GuavaCache(String name, CacheBuilder<Object, Object> builder, boolean allowNullValues) {
		this.name = checkNotNull(name, "name is required");
		this.allowNullValues = allowNullValues;
		this.store = builder.build();
	}

	@Override
	public String getName() {
		return this.name;
	}

	public boolean isAllowNullValues() {
		return allowNullValues;
	}

	@Override
	public com.google.common.cache.Cache<Object, Object> getNativeCache() {
		return this.store;
	}

	@Override
	public ValueWrapper get(Object key) {
		Object value = this.store.getIfPresent(key);
		return (value != null ? new SimpleValueWrapper(fromStoreValue(value)) : null);
	}

	@Override
	public void put(Object key, Object value) {
		this.store.put(key, toStoreValue(value));
	}

	@Override
	public void evict(Object key) {
		this.store.invalidate(key);
	}

	@Override
	public void clear() {
		this.store.invalidateAll();
	}

	/**
	 * Convert the given value from the internal store to a user value
	 * returned from the get method (adapting {@code null}).
	 * @param storeValue the store value
	 * @return the value to return to the user
	 */
	protected Object fromStoreValue(Object storeValue) {
		if (this.allowNullValues && storeValue == NULL_HOLDER) {
			return null;
		}
		return storeValue;
	}

	/**
	 * Convert the given user value, as passed into the put method,
	 * to a value in the internal store (adapting {@code null}).
	 * @param userValue the given user value
	 * @return the value to store
	 */
	protected Object toStoreValue(Object userValue) {
		if (this.allowNullValues && userValue == null) {
			return NULL_HOLDER;
		}
		return userValue;
	}

	@SuppressWarnings("serial")
	private static class NullHolder implements Serializable {

	}

}