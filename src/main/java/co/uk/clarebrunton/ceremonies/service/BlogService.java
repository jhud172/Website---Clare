package co.uk.clarebrunton.ceremonies.service;

import co.uk.clarebrunton.ceremonies.model.BlogPost;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogService {

	private final List<BlogPost> posts = List.of(
			new BlogPost(
					"creating-a-wedding-ceremony-that-still-feels-like-you",
					"Creating a wedding ceremony that still feels like you",
					"An outline for couples who want their ceremony to feel calm, personal and well-paced from the first welcome to the final exit.",
					"Weddings",
					"4 min read",
					"May 2026",
					"Editorial guidance for modern wedding ceremonies",
					List.of(
							"The most memorable ceremonies are often the least over-written. Start with the tone you want guests to feel when the ceremony begins, then build the words, music and transitions around that emotional direction.",
							"Think about rhythm as much as wording. A strong ceremony moves naturally between welcome, story, promises and celebration without feeling rushed or over-explained.",
							"Use this article as a placeholder for Clare's real planning advice, personal approach and examples from previous ceremonies."
					)
			),
			new BlogPost(
					"how-to-shape-a-funeral-tribute-with-warmth-and-clarity",
					"How to shape a funeral tribute with warmth and clarity",
					"Practical notes for families who want a tribute that is honest, sensitive and steady for everyone in the room.",
					"Funerals",
					"5 min read",
					"May 2026",
					"Supportive planning for funeral and memorial services",
					List.of(
							"A thoughtful tribute makes space for both grief and gratitude. The strongest scripts usually balance practical structure with a few details that bring the person fully into the room.",
							"When planning a funeral or memorial service, it helps to gather stories early, decide who is speaking and agree where music, readings or moments of reflection should sit.",
							"This placeholder article can later be replaced with Clare's own process, examples of family collaboration and any faith or non-faith approach she offers."
					)
			),
			new BlogPost(
					"questions-to-ask-before-booking-a-celebrant",
					"Questions to ask before booking a celebrant",
					"A short checklist for choosing the right person to lead a wedding, funeral or milestone ceremony with confidence.",
					"Planning",
					"3 min read",
					"June 2026",
					"Choosing the right celebrant for the occasion",
					List.of(
							"Ask how the ceremony is shaped, how revisions are handled and what level of support is included between the first conversation and the ceremony day itself.",
							"For marriage-related services in particular, confirm exactly what is legally included. Some celebrant services are ceremonial only, while legal registration can require a separate registrar process depending on the area and licence held.",
							"Use this piece as a placeholder until the client finalises her exact service boundaries, pricing model and legal position."
					)
			)
	);

	public List<BlogPost> findAll() {
		return posts;
	}

	public List<BlogPost> findFeatured() {
		return posts.stream().limit(2).toList();
	}

	public BlogPost findBySlug(String slug) {
		return posts.stream()
				.filter(post -> post.slug().equals(slug))
				.findFirst()
				.orElse(null);
	}

}
