package co.uk.clarebrunton.ceremonies.service;

import java.nio.charset.StandardCharsets;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import co.uk.clarebrunton.ceremonies.config.SiteProperties;
import co.uk.clarebrunton.ceremonies.model.InquiryForm;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

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

	public void handleInquiry(InquiryForm inquiryForm, List<MultipartFile> attachments) {
		String recipient = StringUtils.hasText(notificationEmail) ? notificationEmail : siteProperties.getContactEmail();
		JavaMailSender mailSender = mailSenderProvider.getIfAvailable();
		List<MultipartFile> safeAttachments = attachments == null ? List.of() : attachments;

		if (mailSender == null || !StringUtils.hasText(recipient)) {
			logger.info("Inquiry received without outbound email configuration: {} / {} / {} / {} attachment(s)",
					inquiryForm.getFullName(),
					inquiryForm.getServiceType(),
					inquiryForm.getEmail(),
					safeAttachments.size());
			return;
		}

		try {
			mailSender.send(createAdminMessage(mailSender, recipient, inquiryForm, safeAttachments));
			mailSender.send(createConfirmationMessage(inquiryForm));
		}
		catch (MailException | MessagingException exception) {
			logger.error("Inquiry email could not be sent. Check SMTP settings before going live.", exception);
		}
	}

	public void handleInquiry(InquiryForm inquiryForm) {
		handleInquiry(inquiryForm, List.of());
	}

	private MimeMessage createAdminMessage(JavaMailSender mailSender,
			String recipient,
			InquiryForm inquiryForm,
			List<MultipartFile> attachments) throws MessagingException {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, !attachments.isEmpty(), StandardCharsets.UTF_8.name());

		if (StringUtils.hasText(siteProperties.getFromEmail())) {
			helper.setFrom(siteProperties.getFromEmail());
		}
		helper.setTo(recipient);
		helper.setReplyTo(inquiryForm.getEmail());
		helper.setSubject("New ceremony enquiry: " + inquiryForm.getServiceType());
		helper.setText("""
				A new website enquiry has been submitted.

				Name: %s
				Email: %s
				Phone: %s
				Service: %s
				Event date: %s
				Venue: %s
				Attachments: %s

				Message:
				%s
				""".formatted(
				inquiryForm.getFullName(),
				inquiryForm.getEmail(),
				inquiryForm.getPhone(),
				inquiryForm.getServiceType(),
				inquiryForm.getEventDate() != null ? inquiryForm.getEventDate() : "Not supplied",
				inquiryForm.getVenue(),
				attachments.isEmpty() ? "None" : attachments.size() + " file(s) attached",
				inquiryForm.getMessage()
		));

		for (MultipartFile attachment : attachments) {
			String filename = StringUtils.hasText(attachment.getOriginalFilename())
					? StringUtils.cleanPath(attachment.getOriginalFilename())
					: "attachment";
			helper.addAttachment(filename, attachment);
		}

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
				Clare Life Ceremonies
				""".formatted(
				resolveGreetingName(inquiryForm),
				inquiryForm.getServiceType().toLowerCase(),
				siteProperties.getContactEmail()
		));
		return message;
	}

	private String resolveGreetingName(InquiryForm inquiryForm) {
		String fullName = inquiryForm.getFullName();
		if (!StringUtils.hasText(fullName)) {
			return "there";
		}

		String[] parts = fullName.trim().split("\\s+");
		return parts.length > 0 ? parts[0] : fullName.trim();
	}

}
