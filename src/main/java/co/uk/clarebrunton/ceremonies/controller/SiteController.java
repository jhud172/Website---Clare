package co.uk.clarebrunton.ceremonies.controller;

import co.uk.clarebrunton.ceremonies.model.InquiryForm;
import co.uk.clarebrunton.ceremonies.service.BlogService;
import co.uk.clarebrunton.ceremonies.service.InquiryNotificationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class SiteController {

	private static final List<String> SERVICE_OPTIONS = List.of(
			"Wedding ceremony",
			"Funeral or memorial",
			"Naming ceremony",
			"Vow renewal",
			"Other ceremony"
	);

	private final BlogService blogService;

	private final InquiryNotificationService inquiryNotificationService;

	public SiteController(BlogService blogService, InquiryNotificationService inquiryNotificationService) {
		this.blogService = blogService;
		this.inquiryNotificationService = inquiryNotificationService;
	}

	@ModelAttribute("serviceOptions")
	public List<String> serviceOptions() {
		return SERVICE_OPTIONS;
	}

	@GetMapping("/")
	public String home(Model model) {
		model.addAttribute("pageTitle", "Warm, modern ceremonies for life's biggest moments");
		model.addAttribute("pageDescription", "Clare Brunton Life Ceremonies offers warm, personal weddings, funerals and milestone ceremonies with a premium, modern feel.");
		return "home";
	}

	@GetMapping("/about")
	public String about(Model model) {
		model.addAttribute("pageTitle", "About Clare");
		model.addAttribute("pageDescription", "Meet Clare and discover the calm, caring approach behind Clare Brunton Life Ceremonies.");
		return "about";
	}

	@GetMapping("/ceremonies")
	public String ceremonies(Model model) {
		model.addAttribute("pageTitle", "Ceremonies");
		model.addAttribute("pageDescription", "An overview of Clare's wedding, funeral and milestone ceremony services.");
		return "ceremonies";
	}

	@GetMapping("/weddings")
	public String weddings(Model model) {
		model.addAttribute("pageTitle", "Wedding ceremonies");
		model.addAttribute("pageDescription", "Modern wedding ceremonies shaped with warmth, style and personal detail.");
		return "weddings";
	}

	@GetMapping("/funerals")
	public String funerals(Model model) {
		model.addAttribute("pageTitle", "Funerals and memorials");
		model.addAttribute("pageDescription", "Thoughtful funeral and memorial ceremonies created with warmth, clarity and care.");
		return "funerals";
	}

	@GetMapping("/reviews")
	public String reviews(Model model) {
		model.addAttribute("pageTitle", "Reviews");
		model.addAttribute("pageDescription", "A dedicated page for kind words, feedback and the experience Clare is building her business around.");
		return "reviews";
	}

	@GetMapping("/services")
	public String servicesRedirect() {
		return "redirect:/ceremonies";
	}

	@GetMapping("/blog")
	public String blog(Model model) {
		model.addAttribute("pageTitle", "Journal");
		model.addAttribute("pageDescription", "Placeholder journal posts for wedding, funeral and celebrant planning.");
		model.addAttribute("posts", blogService.findAll());
		return "blog";
	}

	@GetMapping("/blog/{slug}")
	public String blogPost(@PathVariable String slug, Model model) {
		var post = blogService.findBySlug(slug);
		if (post == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}

		model.addAttribute("pageTitle", post.title());
		model.addAttribute("pageDescription", post.excerpt());
		model.addAttribute("post", post);
		model.addAttribute("posts", blogService.findAll());
		return "blog-post";
	}

	@GetMapping("/contact")
	public String contact(Model model) {
		if (!model.containsAttribute("inquiryForm")) {
			model.addAttribute("inquiryForm", new InquiryForm());
		}
		model.addAttribute("pageTitle", "Contact");
		model.addAttribute("pageDescription", "Get in touch with Clare Brunton Life Ceremonies to start planning a wedding, funeral or milestone ceremony.");
		return "contact";
	}

	@PostMapping("/contact")
	public String submitContact(@Valid @ModelAttribute("inquiryForm") InquiryForm inquiryForm,
			BindingResult bindingResult,
			Model model,
			RedirectAttributes redirectAttributes) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("pageTitle", "Contact");
			model.addAttribute("pageDescription", "Get in touch with Clare Brunton Life Ceremonies to start planning a wedding, funeral or milestone ceremony.");
			return "contact";
		}

		inquiryNotificationService.handleInquiry(inquiryForm);
		redirectAttributes.addFlashAttribute("submittedServiceType", inquiryForm.getServiceType());
		return "redirect:/thank-you";
	}

	@GetMapping("/thank-you")
	public String thankYou(Model model) {
		model.addAttribute("pageTitle", "Thank you for getting in touch");
		model.addAttribute("pageDescription", "Confirmation page after submitting an enquiry to Clare Brunton Life Ceremonies.");
		return "thank-you";
	}

	@GetMapping("/privacy")
	public String privacy(Model model) {
		model.addAttribute("pageTitle", "Privacy");
		model.addAttribute("pageDescription", "Privacy information for Clare Brunton Life Ceremonies and how enquiry details are handled.");
		return "privacy";
	}

}
