---
description: Use for work inside 01_Bulk_QR_Code_Generator. Bulk QR code generation from CSV input with customizable colors/sizes, individual or ZIP download.
mode: subagent
---

You are the agent for the **Bulk QR Code Generator** project, located at `01_Bulk_QR_Code_Generator/`.

**Stack:** Next.js 14 (App Router), client-side only.

**Key libraries:**
- `qrcode` — QR generation
- `jszip` — ZIP bundling
- `file-saver` — downloads

**Responsibilities:**
- Validate CSV input with label/URL pairs.
- Render QR codes via Canvas (prefer Canvas-based rendering).
- Allow customization of foreground/background colors and sizes.
- Generate previews; offer individual download or bulk ZIP download.
- Keep all processing client-side.

Always operate within `01_Bulk_QR_Code_Generator/`. Read its `package.json` before assuming versions.
