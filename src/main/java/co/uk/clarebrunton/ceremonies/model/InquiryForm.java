package co.uk.clarebrunton.ceremonies.model;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class InquiryForm {

	@NotBlank(message = "Please add a first name.")
	private String firstName;

	@NotBlank(message = "Please add a last name.")
	private String lastName;

	@NotBlank(message = "Please add an email address.")
	@Email(message = "Please use a valid email address.")
	private String email;

	@NotBlank(message = "Please add a phone number.")
	private String phone;

	@NotBlank(message = "Please choose the type of ceremony.")
	private String serviceType;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate eventDate;

	@NotBlank(message = "Please add the venue or location.")
	private String venue;

	@NotBlank(message = "Please tell us a little about the ceremony.")
	@Size(min = 20, max = 2000, message = "Please give between 20 and 2000 characters.")
	private String message;

	@AssertTrue(message = "Please confirm that you are happy for us to handle your details.")
	private boolean privacyAccepted;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public LocalDate getEventDate() {
		return eventDate;
	}

	public void setEventDate(LocalDate eventDate) {
		this.eventDate = eventDate;
	}

	public String getVenue() {
		return venue;
	}

	public void setVenue(String venue) {
		this.venue = venue;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isPrivacyAccepted() {
		return privacyAccepted;
	}

	public void setPrivacyAccepted(boolean privacyAccepted) {
		this.privacyAccepted = privacyAccepted;
	}

}
