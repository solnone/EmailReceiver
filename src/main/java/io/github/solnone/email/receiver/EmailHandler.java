package io.github.solnone.email.receiver;

import java.io.IOException;
import java.util.List;

import org.apache.commons.mail2.jakarta.util.MimeMessageParser;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import jakarta.activation.DataSource;
import jakarta.mail.BodyPart;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.internet.MimeUtility;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmailHandler {

    @Autowired
    private ImapProperties imapProperties;

    @ServiceActivator(inputChannel = "incomingEmailsChannel")
    public void receive(Message<MimeMessage> message) {
        MimeMessage mimeMessage = message.getPayload();
        try {
            if (imapProperties.isJsoup()) {
                String subject = mimeMessage.getSubject();
                String sender = MimeUtility.decodeText(mimeMessage.getFrom()[0].toString());
                log.info("Received email: " + subject + " from: " + sender);
                log.info("ContentType: " + mimeMessage.getContentType());
                log.info("Body:");
                log.info(getTextFromMessage(mimeMessage));
            } else {
                MimeMessageParser mimeMessageParser = new MimeMessageParser(mimeMessage);
                mimeMessageParser.parse();
                logMail(mimeMessageParser);
                logAttachments(mimeMessageParser);
            }
            log.info("----------------------------------------------------------------");

        } catch (Exception e) {
            log.trace(e.getMessage(), e);
        }
    }

    private void logMail(MimeMessageParser parser) throws Exception {
        log.info("From: {} To: {} Subject: {}",
                MimeUtility.decodeText(parser.getFrom()),
                MimeUtility.decodeText(parser.getTo().get(0).toString()),
                parser.getSubject());
        log.info("Mail content: {}", parser.getPlainContent());
    }

    private void logAttachments(MimeMessageParser parser) {
        List<DataSource> attachments = parser.getAttachmentList();
        log.info("Email has {} attachment files", attachments.size());
        attachments.forEach(dataSource -> {
            String fileName = dataSource.getName();
            if (fileName != null && fileName.length() > 0) {
                log.info("fileName: {}", fileName);
            }
        });
    }

    private String parseBodyPart(BodyPart bodyPart) throws MessagingException, IOException {
        if (bodyPart.isMimeType("text/html")) {
            return Jsoup.parse(bodyPart.getContent().toString()).text();
        }
        if (bodyPart.getContent() instanceof MimeMultipart) {
            return getTextFromMimeMultipart((MimeMultipart) bodyPart.getContent());
        }
        return "";
    }

    private String getTextFromMimeMultipart(MimeMultipart mimeMultipart) throws IOException, MessagingException {
        var sb = new StringBuilder('\n');
        for (int i = 0; i < mimeMultipart.getCount(); i++) {
            BodyPart bodyPart = mimeMultipart.getBodyPart(i);
            if (bodyPart.isMimeType("text/plain")) {
                sb.append('\n').append(bodyPart.getContent());
            } else {
                sb.append('\n').append(parseBodyPart(bodyPart));
            }
        }
        return sb.toString();
    }

    private String getTextFromMessage(MimeMessage message) throws MessagingException, IOException {
        var content = message.getContent();
        if (message.isMimeType("multipart/*")) {
            MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
            return getTextFromMimeMultipart(mimeMultipart);
        }
        return content.toString();
    }
}
