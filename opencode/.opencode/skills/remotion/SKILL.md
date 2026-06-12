---
name: remotion
description: Build and render programmatic videos using Remotion (React for video)
license: MIT
metadata:
  audience: developers
  framework: remotion
---

## What I do
- Scaffold Remotion projects with `npx create-video`
- Create compositions, sequences, and scenes in React
- Use `<Sequence>`, `<AbsoluteFill>`, `<Video>`, `<Audio>`, `<Img>`, `<spring>` APIs
- Manage <Composition> config (fps, duration, dimensions) in `src/Root.tsx`
- Set up audio visualization, transitions, captions, and animations
- Render locally with `npx remotion render` or server-side with `@remotion/renderer`
- Configure Lambda rendering with `@remotion/lambda` CLI
- Optimize bundle size, codec selection, and performance

## When to use me
Use this skill when:
- Creating a new Remotion video project
- Adding or editing compositions, scenes, or visual effects
- Rendering/output configuration (codec, CRF, resolution)
- Setting up server-side or cloud (Lambda) rendering

## Key package info
- Install: `npm i remotion @remotion/cli`
- Render: `npx remotion render <composition-id> out/video.mp4`
- Studio: `npx remotion studio`
- Lambda deploy: `npx remotion lambda functions deploy`
- API docs: https://remotion.dev/docs/
