# Scenery

> Describe a place. Hear it come to life.

Scenery is an Android app that generates immersive sound environments from natural language prompts. Type a scene — a rainy Parisian café, a medieval tavern, a Siberian train at night — and the app composes a layered soundscape for it using AI.

Built with Jetpack Compose and the Gemini API.

---

## How it works

1. You describe a scene in natural language
2. The prompt is sent to Gemini, which selects and mixes sounds from a curated local library
3. The app plays the layers simultaneously, each with individual volume control
4. A timer stops playback automatically when you're done

---

## Features

- **AI-generated soundscapes** — Gemini picks the right sounds and volumes for any scene you describe
- **Layered mixer** — control the volume of each sound layer independently
- **Timer** — set 25, 45, or 60-minute sessions, or let it play indefinitely
- **Quick suggestions** — tap a prompt to get started instantly

---

## Tech stack

| Layer | Technology |
|---|---|
| UI | Jetpack Compose |
| Audio | MediaPlayer |
| AI | Gemini 2.5 Flash 8B |
| HTTP | Retrofit + Moshi |
| Architecture | ViewModel + StateFlow |

---

## Roadmap

The current version uses a local sound library mapped directly by the AI. The next iteration will introduce a full backend:

- **FastAPI** backend with a curated sound database
- **MongoDB Atlas** with vector search
- **Semantic retrieval** — sounds are matched by embedding similarity, not hardcoded IDs
- The AI will receive semantically ranked candidates and compose the final mix

---

## Screenshots

_Coming soon_

---

## License

MIT
