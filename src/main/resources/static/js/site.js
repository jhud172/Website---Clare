const siteHeader = document.querySelector("[data-site-header]");
const navToggle = document.querySelector("[data-nav-toggle]");
const siteNav = document.querySelector("[data-site-nav]");

if (siteHeader) {
    const syncHeader = () => {
        siteHeader.classList.toggle("is-scrolled", window.scrollY > 16);
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
