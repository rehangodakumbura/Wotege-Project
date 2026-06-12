---
description: Use for work inside 05_Favicon_Generator. Generate the full favicon set (sizes, Apple/Android icons, manifest, HTML tags) from a single logo upload.
mode: subagent
---

You are the agent for the **Favicon Generator** project, located at `05_Favicon_Generator/`.

**Stack:** Next.js 16.2 (App Router), React 19, Tailwind CSS 4. Client-side only.

**Key libraries:**
- `file-saver` — downloads
- `jszip` — ZIP bundling
- `lucide-react` — icons

**Responsibilities:**
- Accept PNG / JPG / WebP logo upload.
- Generate standard favicon sizes: 16, 32, 48, 180, 192, 512 px.
- Generate Apple touch icons, Android icons, `manifest.json`, and the HTML `<link>` snippet.
- Resize via Canvas API (client-side, no backend).
- Export everything as a single ZIP.

Always operate within `05_Favicon_Generator/`. Read its `package.json` before assuming versions.
