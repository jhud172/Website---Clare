package co.uk.clarebrunton.ceremonies;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import co.uk.clarebrunton.ceremonies.controller.SiteController;
import co.uk.clarebrunton.ceremonies.model.InquiryForm;
import co.uk.clarebrunton.ceremonies.service.BlogService;
import co.uk.clarebrunton.ceremonies.service.InquiryNotificationService;

class SiteControllerUnitTest {

	private final InquiryNotificationService inquiryNotificationService = mock(InquiryNotificationService.class);
	private final SiteController controller = new SiteController(new BlogService(), inquiryNotificationService);

	@Test
	void launchRoutesReturnExpectedViews() {
		Model model = new ExtendedModelMap();

		assertThat(controller.home(model)).isEqualTo("home");
		assertThat(controller.about(new ExtendedModelMap())).isEqualTo("about");
		assertThat(controller.services(new ExtendedModelMap())).isEqualTo("ceremonies");
		assertThat(controller.weddings(new ExtendedModelMap())).isEqualTo("weddings");
		assertThat(controller.funerals(new ExtendedModelMap())).isEqualTo("funerals");
		assertThat(controller.reviews(new ExtendedModelMap())).isEqualTo("reviews");
		assertThat(controller.contact(new ExtendedModelMap())).isEqualTo("contact");
		assertThat(controller.privacy(new ExtendedModelMap())).isEqualTo("privacy");
		assertThat(controller.thankYou(new ExtendedModelMap())).isEqualTo("thank-you");
		assertThat(controller.ceremoniesRedirect()).isEqualTo("redirect:/services");
	}

	@Test
	void contactAddsInquiryFormWhenMissing() {
		Model model = new ExtendedModelMap();

		controller.contact(model);

		assertThat(model.getAttribute("inquiryForm")).isInstanceOf(InquiryForm.class);
	}

	@Test
	void submitContactReturnsContactViewWhenBindingHasErrors() {
		InquiryForm inquiryForm = new InquiryForm();
		BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(inquiryForm, "inquiryForm");
		bindingResult.rejectValue("fullName", "required", "Please add your full name.");

		String view = controller.submitContact(
				inquiryForm,
				bindingResult,
				new ExtendedModelMap(),
				new RedirectAttributesModelMap()
		);

		assertThat(view).isEqualTo("contact");
		verifyNoInteractions(inquiryNotificationService);
	}

	@Test
	void submitContactRedirectsWhenBindingIsValid() {
		InquiryForm inquiryForm = new InquiryForm();
		inquiryForm.setFullName("James Hudson");
		inquiryForm.setEmail("james@example.com");
		inquiryForm.setPhone("07123456789");
		inquiryForm.setServiceType("Wedding ceremony");
		inquiryForm.setVenue("The Mill Barns");
		inquiryForm.setMessage("We are looking for a warm, modern wedding ceremony with a personal tone.");
		inquiryForm.setPrivacyAccepted(true);

		BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(inquiryForm, "inquiryForm");

		String view = controller.submitContact(
				inquiryForm,
				bindingResult,
				new ExtendedModelMap(),
				new RedirectAttributesModelMap()
		);

		assertThat(view).isEqualTo("redirect:/thank-you");
		verify(inquiryNotificationService).handleInquiry(inquiryForm);
	}
}
