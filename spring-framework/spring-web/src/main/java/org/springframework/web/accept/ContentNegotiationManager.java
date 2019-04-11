/*
 * Copyright 2002-2017 the original author or authors.
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

package org.springframework.web.accept;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.context.request.NativeWebRequest;

/**
 * Central class to determine requested {@linkplain MediaType media types}
 * for a request. This is done by delegating to a list of configured
 * {@code ContentNegotiationStrategy} instances.
 *
 * <p>Also provides methods to look up file extensions for a media type.
 * This is done by delegating to the list of configured
 * {@code MediaTypeFileExtensionResolver} instances.
 *
 * @author Rossen Stoyanchev
 * @since 3.2
 */
public class ContentNegotiationManager implements ContentNegotiationStrategy, MediaTypeFileExtensionResolver {

	private static final List<MediaType> MEDIA_TYPE_ALL = Collections.<MediaType>singletonList(MediaType.ALL);


	private final List<ContentNegotiationStrategy> strategies = new ArrayList<ContentNegotiationStrategy>();

	private final Set<MediaTypeFileExtensionResolver> resolvers = new LinkedHashSet<MediaTypeFileExtensionResolver>();


	/**
	 * Create an instance with the given list of
	 * {@code ContentNegotiationStrategy} strategies each of which may also be
	 * an instance of {@code MediaTypeFileExtensionResolver}.
	 * @param strategies the strategies to use
	 */
	public ContentNegotiationManager(ContentNegotiationStrategy... strategies) {
		this(Arrays.asList(strategies));
	}

	/**
	 * A collection-based alternative to
	 * {@link #ContentNegotiationManager(ContentNegotiationStrategy...)}.
	 * @param strategies the strategies to use
	 */
	public ContentNegotiationManager(Collection<ContentNegotiationStrategy> strategies) {
		Assert.notEmpty(strategies, "At least one ContentNegotiationStrategy is expected");
		this.strategies.addAll(strategies);
		for (ContentNegotiationStrategy strategy : this.strategies) {
			if (strategy instanceof MediaTypeFileExtensionResolver) {
				this.resolvers.add((MediaTypeFileExtensionResolver) strategy);
			}
		}
	}

	/**
	 * Create a default instance with a {@link HeaderContentNegotiationStrategy}.
	 */
	public ContentNegotiationManager() {
		this(new HeaderContentNegotiationStrategy());
	}


	/**
	 * Return the configured content negotiation strategies.
	 * @since 3.2.16
	 */
	public List<ContentNegotiationStrategy> getStrategies() {
		return this.strategies;
	}

	/**
	 * Find a {@code ContentNegotiationStrategy} of the given type.
	 * @param strategyType the strategy type
	 * @return the first matching strategy or {@code null}.
	 * @since 4.3
	 */
	@SuppressWarnings("unchecked")
	public <T extends ContentNegotiationStrategy> T getStrategy(Class<T> strategyType) {
		for (ContentNegotiationStrategy strategy : getStrategies()) {
			if (strategyType.isInstance(strategy)) {
				return (T) strategy;
			}
		}
		return null;
	}

	/**
	 * Register more {@code MediaTypeFileExtensionResolver} instances in addition
	 * to those detected at construction.
	 * @param resolvers the resolvers to add
	 */
	public void addFileExtensionResolvers(MediaTypeFileExtensionResolver... resolvers) {
		this.resolvers.addAll(Arrays.asList(resolvers));
	}

	@Override
	public List<MediaType> resolveMediaTypes(NativeWebRequest request) throws HttpMediaTypeNotAcceptableException {
		for (ContentNegotiationStrategy strategy : this.strategies) {
			List<MediaType> mediaTypes = strategy.resolveMediaTypes(request);
			if (mediaTypes.isEmpty() || mediaTypes.equals(MEDIA_TYPE_ALL)) {
				continue;
			}
			return mediaTypes;
		}
		return Collections.emptyList();
	}

	@Override
	public List<String> resolveFileExtensions(MediaType mediaType) {
		Set<String> result = new LinkedHashSet<String>();
		for (MediaTypeFileExtensionResolver resolver : this.resolvers) {
			result.addAll(resolver.resolveFileExtensions(mediaType));
		}
		return new ArrayList<String>(result);
	}

	/**
	 * {@inheritDoc}
	 * <p>At startup this method returns extensions explicitly registered with
	 * either {@link PathExtensionContentNegotiationStrategy} or
	 * {@link ParameterContentNegotiationStrategy}. At runtime if there is a
	 * "path extension" strategy and its
	 * {@link PathExtensionContentNegotiationStrategy#setUseJaf(boolean)
	 * useJaf} property is set to "true", the list of extensions may
	 * increase as file extensions are resolved via JAF and cached.
	 */
	@Override
	public List<String> getAllFileExtensions() {
		Set<String> result = new LinkedHashSet<String>();
		for (MediaTypeFileExtensionResolver resolver : this.resolvers) {
			result.addAll(resolver.getAllFileExtensions());
		}
		return new ArrayList<String>(result);
	}

}