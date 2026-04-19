const siteHeader = document.querySelector("[data-site-header]");
const navToggle = document.querySelector("[data-nav-toggle]");
const siteNav = document.querySelector("[data-site-nav]");
const themeToggle = document.querySelector("[data-theme-toggle]");
const themeToggleLabel = document.querySelector("[data-theme-toggle-label]");
const fileInputs = document.querySelectorAll("[data-file-input]");
const THEME_STORAGE_KEY = "clare-theme";
const revealSelectors = [
    ".page-hero-grid > *",
    ".hero-copy",
    ".hero .visual-stage",
    ".section-heading",
    ".path-panel",
    ".detail-card",
    ".quote-card",
    ".package-card",
    ".gallery-card",
    ".story-copy",
    ".split-layout .visual-stage",
    ".process-list li",
    ".faq-list article",
    ".contact-panel",
    ".contact-form",
    ".cta-panel"
];

const applyTheme = (theme) => {
    document.documentElement.dataset.theme = theme;

    if (!themeToggle || !themeToggleLabel) {
        return;
    }

    const nextTheme = theme === "dark" ? "light" : "dark";
    const nextLabel = nextTheme === "dark" ? "Dark mode" : "Light mode";

    themeToggle.setAttribute("aria-pressed", String(theme === "dark"));
    themeToggle.setAttribute("aria-label", `Switch to ${nextLabel.toLowerCase()}`);
    themeToggleLabel.textContent = nextLabel;
};

const resolveTheme = () => {
    const storedTheme = window.localStorage.getItem(THEME_STORAGE_KEY);
    if (storedTheme === "light" || storedTheme === "dark") {
        return storedTheme;
    }

    return window.matchMedia("(prefers-color-scheme: dark)").matches ? "dark" : "light";
};

applyTheme(resolveTheme());

if (themeToggle) {
    themeToggle.addEventListener("click", () => {
        const nextTheme = document.documentElement.dataset.theme === "dark" ? "light" : "dark";
        window.localStorage.setItem(THEME_STORAGE_KEY, nextTheme);
        applyTheme(nextTheme);
    });

    window.matchMedia("(prefers-color-scheme: dark)").addEventListener("change", (event) => {
        if (window.localStorage.getItem(THEME_STORAGE_KEY)) {
            return;
        }

        applyTheme(event.matches ? "dark" : "light");
    });
}

if (siteHeader) {
    let lastScrollY = window.scrollY;

    const syncHeader = () => {
        const currentScrollY = window.scrollY;
        siteHeader.classList.toggle("is-scrolled", currentScrollY > 16);

        if (window.innerWidth > 1040) {
            const scrollingDown = currentScrollY > lastScrollY;
            const scrollingUp = currentScrollY < lastScrollY;

            if (currentScrollY > 180 && scrollingDown) {
                siteHeader.classList.add("is-condensed");
            }
            else if (currentScrollY < 96 || scrollingUp) {
                siteHeader.classList.remove("is-condensed");
            }
        }
        else {
            siteHeader.classList.remove("is-condensed");
        }

        lastScrollY = currentScrollY;
    };

    syncHeader();
    window.addEventListener("scroll", syncHeader, { passive: true });
}

if (navToggle && siteNav) {
    const closeNav = () => {
        siteNav.classList.remove("is-open");
        navToggle.setAttribute("aria-expanded", "false");
    };

    navToggle.addEventListener("click", () => {
        const isOpen = siteNav.classList.toggle("is-open");
        navToggle.setAttribute("aria-expanded", String(isOpen));
    });

    siteNav.querySelectorAll("a").forEach((link) => {
        link.addEventListener("click", closeNav);
    });

    window.addEventListener("keydown", (event) => {
        if (event.key === "Escape") {
            closeNav();
        }
    });

    window.addEventListener("resize", () => {
        if (window.innerWidth > 1040) {
            closeNav();
        }
    });
}

const revealTargets = Array.from(new Set(
    revealSelectors.flatMap((selector) => Array.from(document.querySelectorAll(selector)))
));

if (window.matchMedia("(prefers-reduced-motion: reduce)").matches) {
    revealTargets.forEach((element) => {
        element.classList.add("is-visible");
    });
}
else if (revealTargets.length > 0) {
    revealTargets.forEach((element, index) => {
        element.classList.add("reveal-ready");
        element.classList.add(`reveal-delay-${index % 4}`);
    });

    const revealObserver = new IntersectionObserver((entries, observer) => {
        entries.forEach((entry) => {
            if (!entry.isIntersecting) {
                return;
            }

            entry.target.classList.add("is-visible");
            observer.unobserve(entry.target);
        });
    }, {
        threshold: 0.18,
        rootMargin: "0px 0px -8% 0px"
    });

    revealTargets.forEach((element) => {
        revealObserver.observe(element);
    });
}

const formatFileSize = (size) => {
    if (size < 1024 * 1024) {
        return `${Math.round(size / 1024)} KB`;
    }

    return `${(size / (1024 * 1024)).toFixed(1)} MB`;
};

fileInputs.forEach((input) => {
    const targetId = input.getAttribute("data-file-list-target");
    const fileList = targetId ? document.getElementById(targetId) : null;
    const dropzone = input.closest("[data-file-dropzone]");

    const syncFiles = () => {
        if (!dropzone || !fileList) {
            return;
        }

        const files = Array.from(input.files || []);
        dropzone.classList.toggle("has-files", files.length > 0);
        fileList.innerHTML = "";
        fileList.hidden = files.length === 0;

        files.forEach((file) => {
            const item = document.createElement("li");
            item.textContent = `${file.name} · ${formatFileSize(file.size)}`;
            fileList.appendChild(item);
        });
    };

    syncFiles();
    input.addEventListener("change", syncFiles);
});
