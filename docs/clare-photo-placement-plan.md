# Clare Photo Placement Plan

Date: 20 April 2026

Purpose:
Store Clare's newly supplied portraits in the repository and map them to the best live use on the current site build.

## Photos Now Stored

Archive copy:
- `docs/client-assets/clare-portraits/clare1.jpg`
- `docs/client-assets/clare-portraits/clare2.jpg`
- `docs/client-assets/clare-portraits/clare3.jpg`
- `docs/client-assets/clare-portraits/clare4.jpg`
- `docs/client-assets/clare-portraits/clare5.jpg`
- `docs/client-assets/clare-portraits/clare6.jpg`

Public-ready copy:
- `src/main/resources/static/images/clare/weddings-confetti.jpg`
- `src/main/resources/static/images/clare/weddings-confetti-alt.jpg`
- `src/main/resources/static/images/clare/wedding-symbolism-ribbons.jpg`
- `src/main/resources/static/images/clare/clare-folder-portrait.jpg`
- `src/main/resources/static/images/clare/funeral-portrait-blazer.jpg`
- `src/main/resources/static/images/clare/funeral-portrait-black.jpg`

## Recommended Usage

### Weddings
- Use `weddings-confetti.jpg` as the first Clare-led image on the weddings page.
- Keep `weddings-confetti-alt.jpg` as the backup crop or alternate placement if the first image needs changing later.
- Use `wedding-symbolism-ribbons.jpg` in the part of the weddings page that talks about symbolism, rituals, or personal ceremony elements.

### Funerals
- Use `funeral-portrait-blazer.jpg` and `funeral-portrait-black.jpg` for the funerals page, especially the calmer support-led sections.
- Preferred order:
  - `funeral-portrait-blazer.jpg` for a main funeral portrait or hero-support block.
  - `funeral-portrait-black.jpg` for a secondary supporting panel or detail section.

### About / Contact / General Brand Use
- Use `clare-folder-portrait.jpg` as the main general portrait for Clare on about/contact if a professional, direct, welcoming image is needed.
- This image also works well for a ceremonies overview page or for any section explaining Clare's role and process.

## Immediate Build Tasks

1. Implemented: replaced the temporary about placeholder with Clare's own portrait.
2. Implemented: added the confetti portrait to the weddings page in the first Clare-specific visual position.
3. Implemented: added the ribbons portrait into a wedding symbolism section.
4. Implemented: added both black-outfit portraits into the funerals page so the tone is led by Clare's actual photography.
5. Current state: the existing real wedding gallery remains live until Clare supplies her own ceremony photography from completed weddings.

## Future Photo Swaps

- Once Clare has real wedding ceremony photography from her own work, the first few live wedding photos can be swapped in without restructuring the site.
- The site already uses semantic image paths, so replacing early launch imagery should be a straight asset-and-template update rather than a redesign.
- Suggested future folder target for Clare's own live job photography:
  - `docs/client-assets/clare-weddings/`
  - `src/main/resources/static/images/clare-weddings/`

## Notes

- `clare1.jpg` and `clare6.jpg` are visually the same confetti setup, so one should be treated as the main live image and the other as a backup/archive version.
- All six supplied files are now stored inside the repository and ready for live placement.
