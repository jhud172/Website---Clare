package co.uk.clarebrunton.ceremonies.controller;

import co.uk.clarebrunton.ceremonies.config.SiteProperties;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class SiteModelAdvice {

	private final SiteProperties siteProperties;

	public SiteModelAdvice(SiteProperties siteProperties) {
		this.siteProperties = siteProperties;
	}

	@ModelAttribute
	public void addSiteData(Model model) {
		model.addAttribute("site", siteProperties);
	}

	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public String handleUploadTooLarge(RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("attachmentError", "Files can be up to 5 MB each and 15 MB in total.");
		return "redirect:/contact";
	}

}
