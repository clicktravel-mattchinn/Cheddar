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
package com.clicktravel.cheddar.infrastructure.messaging;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;

import com.clicktravel.common.random.Randoms;

public class MessageRouterTest {

    @Test
    public void shouldSendMessageOnCorrectRoute_onHandle() throws Exception {
        // Given
        final Message mockMessage = mock(Message.class);
        final String messageType = Randoms.randomString();
        when(mockMessage.getType()).thenReturn(messageType);

        final MessageListener mockMessageListener = mock(MessageListener.class);
        final MessageRouter messageRouter = new MessageRouter(mockMessageListener);

        final MessageSender expectedMessageSender = mock(MessageSender.class);
        messageRouter.addRoute(messageType, expectedMessageSender);
        final Collection<MessageSender> unusedMessageSenders = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            final MessageSender messageSender = mock(MessageSender.class);
            messageRouter.addRoute(Randoms.randomString(), messageSender);
            unusedMessageSenders.add(messageSender);
        }

        // When
        messageRouter.handle(mockMessage);

        // Then
        verify(expectedMessageSender).sendMessage(mockMessage);
        for (final MessageSender unusedMessageSender : unusedMessageSenders) {
            verifyZeroInteractions(unusedMessageSender);
        }
    }

    @Test
    public void shouldSendMessageOnMultipleRoutes_onHandle() throws Exception {
        // Given
        final Message mockMessage = mock(Message.class);
        final String messageType = Randoms.randomString();
        when(mockMessage.getType()).thenReturn(messageType);

        final MessageListener mockMessageListener = mock(MessageListener.class);
        final MessageRouter messageRouter = new MessageRouter(mockMessageListener);
        final Collection<MessageSender> messageSenders = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            final MessageSender messageSender = mock(MessageSender.class);
            messageSenders.add(messageSender);
            messageRouter.addRoute(messageType, messageSender);
        }

        // When
        messageRouter.handle(mockMessage);

        // Then
        for (final MessageSender messageSender : messageSenders) {
            verify(messageSender).sendMessage(mockMessage);
        }
    }

    @Test
    public void shoudIgnoreUnroutedMessageType_onHandle() {
        // Given
        final Message mockMessage = mock(Message.class);
        final String messageType = Randoms.randomString();
        when(mockMessage.getType()).thenReturn(messageType);

        final MessageListener mockMessageListener = mock(MessageListener.class);
        final MessageRouter messageRouter = new MessageRouter(mockMessageListener);

        // When
        Exception thrownException = null;
        try {
            messageRouter.handle(mockMessage);
        } catch (final Exception e) {
            thrownException = e;
        }

        // Then
        assertNull(thrownException);
    }
}
