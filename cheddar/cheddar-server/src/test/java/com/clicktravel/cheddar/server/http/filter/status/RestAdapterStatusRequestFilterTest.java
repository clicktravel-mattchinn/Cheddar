/*
 * Copyright 2014 Click Travel Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.clicktravel.cheddar.server.http.filter.status;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import javax.ws.rs.container.ContainerRequestContext;

import org.junit.Test;

import com.clicktravel.cheddar.server.application.status.RestAdapterStatusHolder;

public class RestAdapterStatusRequestFilterTest {

    @Test
    public void shouldInvokeProcessingStarted_onFilter() throws Exception {
        // Given
        final RestAdapterStatusHolder mockRestAdapterStatusHolder = mock(RestAdapterStatusHolder.class);
        final RestAdapterStatusRequestFilter restAdapterStatusRequestFilter = new RestAdapterStatusRequestFilter(
                mockRestAdapterStatusHolder);
        final ContainerRequestContext mockRequestContext = mock(ContainerRequestContext.class);

        // When
        restAdapterStatusRequestFilter.filter(mockRequestContext);

        // Then
        verify(mockRestAdapterStatusHolder).requestProcessingStarted();
    }
}
