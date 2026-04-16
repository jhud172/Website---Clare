package co.uk.clarebrunton.ceremonies.controller;

import co.uk.clarebrunton.ceremonies.config.SiteProperties;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

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

}
