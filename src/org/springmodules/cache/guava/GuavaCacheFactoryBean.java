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
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.StringUtils;

/**
 * {@link FactoryBean} for easy configuration of a {@link GuavaCache}.
 * @author Omar Irbouh
 * @since 1.0
 */
public class GuavaCacheFactoryBean
		implements FactoryBean<GuavaCache>, BeanNameAware, InitializingBean {

	private String name = "";

	private boolean allowNullValues = true;

	private String spec;

	private GuavaCache cache;

	public void setName(String name) {
		this.name = name;
	}

	public void setAllowNullValues(boolean allowNullValues) {
		this.allowNullValues = allowNullValues;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	@Override
	public void setBeanName(String name) {
		if (!StringUtils.hasLength(this.name)) {
			this.name = name;
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if (StringUtils.hasText(this.spec)) {
			this.cache = new GuavaCache(this.name, CacheBuilder.from(spec), allowNullValues);
		}
		else {
			this.cache = new GuavaCache(this.name, allowNullValues);
		}
	}

	@Override
	public GuavaCache getObject() throws Exception {
		return this.cache;
	}

	@Override
	public Class<?> getObjectType() {
		return GuavaCache.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

}