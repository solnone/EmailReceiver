package io.github.solnone.email.receiver;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.mail.ImapMailReceiver;
import org.springframework.integration.mail.MailReceiver;
import org.springframework.integration.mail.MailReceivingMessageSource;
import org.springframework.messaging.MessageChannel;

import jakarta.mail.internet.MimeMessage;

@Configuration
public class EmailReceiverConfig {

    @Autowired
    private ImapProperties imapProperties;

    /*
     * Define a channel where we send and get the messages.
     * MessageSource is sending messages to this channel.
     * DirectChannel is a SubscribableChannel.
     */
    @Bean("incomingEmailsChannel")
    public MessageChannel defaultChannel() {
        DirectChannel directChannel = new DirectChannel();
        directChannel.setDatatypes(MimeMessage.class);
        return directChannel;
    }

    @Bean
    public MailReceiver imapMailReceiver() {
        ImapMailReceiver imapMailReceiver = new ImapMailReceiver(imapProperties.getUrl());
        imapMailReceiver.setShouldMarkMessagesAsRead(true);
        imapMailReceiver.setShouldDeleteMessages(false);
        /*
         * Attach content to message because by default the MimeMessage
         * doesn't contain content body.
         */
        imapMailReceiver.setSimpleContent(true);
        imapMailReceiver.setMaxFetchSize(1);

        return imapMailReceiver;
    }

    /*
     * Provide MessageSource of Mails as spring integration Messages from
     * ImapMailReceiver to be sent
     * through incomingEmailsChannel.
     * Poller with defined rate at which the messages are pushed to the channel
     * (if there are any) every 5 sec.
     */
    @Bean
    @InboundChannelAdapter(channel = "incomingEmailsChannel", poller = @Poller(fixedDelay = "#{@imapProperties.interval}"))
    public MailReceivingMessageSource mailMessageSource(MailReceiver mailReceiver) {
        return new MailReceivingMessageSource(mailReceiver);
    }

}
