package co.uk.clarebrunton.ceremonies.model;

import java.util.List;

public record BlogPost(
		String slug,
		String title,
		String excerpt,
		String category,
		String readTime,
		String publishedOn,
		String heroEyebrow,
		List<String> paragraphs
) {
}
