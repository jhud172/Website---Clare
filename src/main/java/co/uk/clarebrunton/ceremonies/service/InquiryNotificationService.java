package co.uk.clarebrunton.ceremonies.service;

import co.uk.clarebrunton.ceremonies.config.SiteProperties;
import co.uk.clarebrunton.ceremonies.model.InquiryForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class InquiryNotificationService {

	private static final Logger logger = LoggerFactory.getLogger(InquiryNotificationService.class);

	private final ObjectProvider<JavaMailSender> mailSenderProvider;

	private final SiteProperties siteProperties;

	@Value("${inquiry.notification-email:}")
	private String notificationEmail;

	public InquiryNotificationService(ObjectProvider<JavaMailSender> mailSenderProvider, SiteProperties siteProperties) {
		this.mailSenderProvider = mailSenderProvider;
		this.siteProperties = siteProperties;
	}

	public void handleInquiry(InquiryForm inquiryForm) {
		String recipient = StringUtils.hasText(notificationEmail) ? notificationEmail : siteProperties.getContactEmail();
		JavaMailSender mailSender = mailSenderProvider.getIfAvailable();

		if (mailSender == null || !StringUtils.hasText(recipient)) {
			logger.info("Inquiry received without outbound email configuration: {} {} / {} / {}",
					inquiryForm.getFirstName(),
					inquiryForm.getLastName(),
					inquiryForm.getServiceType(),
					inquiryForm.getEmail());
			return;
		}

		try {
			mailSender.send(createAdminMessage(recipient, inquiryForm));
			mailSender.send(createConfirmationMessage(inquiryForm));
		}
		catch (MailException exception) {
			logger.error("Inquiry email could not be sent. Check SMTP settings before going live.", exception);
		}
	}

	private SimpleMailMessage createAdminMessage(String recipient, InquiryForm inquiryForm) {
		SimpleMailMessage message = new SimpleMailMessage();
		if (StringUtils.hasText(siteProperties.getFromEmail())) {
			message.setFrom(siteProperties.getFromEmail());
		}
		message.setTo(recipient);
		message.setReplyTo(inquiryForm.getEmail());
		message.setSubject("New ceremony enquiry: " + inquiryForm.getServiceType());
		message.setText("""
				A new website enquiry has been submitted.

				Name: %s %s
				Email: %s
				Phone: %s
				Service: %s
				Event date: %s
				Venue: %s

				Message:
				%s
				""".formatted(
				inquiryForm.getFirstName(),
				inquiryForm.getLastName(),
				inquiryForm.getEmail(),
				inquiryForm.getPhone(),
				inquiryForm.getServiceType(),
				inquiryForm.getEventDate() != null ? inquiryForm.getEventDate() : "Not supplied",
				inquiryForm.getVenue(),
				inquiryForm.getMessage()
		));
		return message;
	}

	private SimpleMailMessage createConfirmationMessage(InquiryForm inquiryForm) {
		SimpleMailMessage message = new SimpleMailMessage();
		if (StringUtils.hasText(siteProperties.getFromEmail())) {
			message.setFrom(siteProperties.getFromEmail());
		}
		message.setTo(inquiryForm.getEmail());
		message.setSubject("Thank you for your enquiry to " + siteProperties.getName());
		message.setText("""
				Hello %s,

				Thank you for getting in touch about your %s.

				This is a quick confirmation to let you know your enquiry has been received safely.

				Clare will review your details and reply personally as soon as she can. If your plans are time-sensitive, you are also welcome to follow up on %s.

				With thanks,
				Clare Brunton Life Ceremonies
				""".formatted(
				inquiryForm.getFirstName(),
				inquiryForm.getServiceType().toLowerCase(),
				siteProperties.getContactEmail()
		));
		return message;
	}

}
