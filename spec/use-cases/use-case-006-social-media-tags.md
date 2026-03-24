# UC-006: Social Media Tags & OG Image

**As a** user sharing the site link on social media, **I want** the shared link to display an attractive preview with title, description, and screenshot **so that** the link looks professional and encourages clicks.

**Status:** Implemented
**Date:** 2026-03-19

---

## Main Flow

- I copy the site URL and paste it into a social media platform (Twitter/X, Facebook, Slack, Discord, LinkedIn, etc.)
- The platform fetches the page and renders a rich preview card
- The preview shows the site title, a description of the cinema, and a screenshot of the front page

---

## Business Rules

| ID | Rule |
|----|------|
| BR-01 | Every page must include Open Graph (`og:`) meta tags: `og:title`, `og:description`, `og:image`, `og:url`, `og:type` |
| BR-02 | Every page must include Twitter Card meta tags: `twitter:card`, `twitter:title`, `twitter:description`, `twitter:image` |
| BR-03 | The OG image must be a screenshot of the front page (the movie browse view), stored as a static asset |
| BR-04 | The OG image should be at least 1200×630 pixels for optimal display across platforms |
| BR-05 | The `og:title` should be the site name: "CineMax" |
| BR-06 | The `og:description` should summarize the app, e.g. "Browse movies, pick your seats, and buy tickets at CineMax cinema" |

---

## Acceptance Criteria

- [ ] HTML `<head>` includes `og:title`, `og:description`, `og:image`, `og:url`, and `og:type` meta tags
- [ ] HTML `<head>` includes `twitter:card`, `twitter:title`, `twitter:description`, and `twitter:image` meta tags
- [ ] `og:image` points to a static screenshot of the front page (at least 1200×630px)
- [ ] The OG image file is committed to the repository and served as a static asset
- [ ] Meta tags are present in the server-rendered HTML (not only after JS hydration) so that crawlers can read them

---

## UI / Routes

No new routes. Meta tags are added to the HTML `<head>` via the application shell.

| Affected area | Notes |
|---------------|-------|
| `Application.java` (AppShellConfigurator) | Add `@Meta` annotations or override `configurePage()` to inject OG/Twitter tags |
| `posters/` or `META-INF/resources/` | Store the OG image screenshot as a static file |
