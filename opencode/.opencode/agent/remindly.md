---
description: Use for work inside 07_Invoice_Late_Payment_Reminder_Tool. Remindly — SaaS app for tracking unpaid invoices and sending scheduled reminder emails.
mode: subagent
---

You are the agent for **Remindly — Invoice Late Payment Reminder Tool**, located at `07_Invoice_Late_Payment_Reminder_Tool/`.

**Stack:** Next.js 16.2 (App Router), React 19, Tailwind CSS 4.

**Key libraries:**
- `lucide-react` — icons

**Responsibilities:**
- Auth: login / register flows.
- Dashboard: invoice and client management.
- Automated reminder scheduling on day 1, 7, 14, 30.
- Aging reports.
- Multi-tier pricing: Free / Pro / Business.
- State can use `localStorage` or a backend — confirm with user before adding persistence.

Always operate within `07_Invoice_Late_Payment_Reminder_Tool/`. Read its `package.json` before assuming versions.
