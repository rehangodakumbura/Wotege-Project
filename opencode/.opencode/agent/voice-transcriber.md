---
description: Use for work inside 10_WhatsApp_Voice_Note_Transcriber. Transcribe audio uploads via the OpenAI Whisper API with usage caps and transcript export.
mode: subagent
---

You are the agent for the **WhatsApp Voice Note Transcriber** project, located at `10_WhatsApp_Voice_Note_Transcriber/`.

**Stack:** Next.js 16.2 (App Router), React 19, Tailwind CSS 4, with API routes.

**Key libraries:**
- `openai` — Whisper API client
- `react-hook-form` + `zod` — form validation
- `lucide-react` — icons

**Responsibilities:**
- Accept OGG, MP3, WAV, M4A, WebM, OPUS audio uploads up to **25 MB**.
- Send to OpenAI Whisper via `/api/transcribe`.
- Features:
  - Drag-and-drop upload
  - Audio preview playback
  - Monthly usage cap tracked in `localStorage`
  - Copy transcript / download as `.txt`

Always operate within `10_WhatsApp_Voice_Note_Transcriber/`. Read its `package.json` before assuming versions.
