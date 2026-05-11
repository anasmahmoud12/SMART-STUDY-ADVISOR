# Smart Study Advisor 

Intelligent Academic Advising Backend using Django, Gemini AI, and Prolog

---

#  Overview

Smart Study Advisor is an intelligent academic advising backend built with Django that combines:

-  Generative AI
-  Rule-Based Logic
- University Prerequisite Systems

The system helps students receive accurate, context-aware course recommendations while strictly enforcing academic rules.

---

# Core Modes

| Mode | Description |
|---|---|
|  AI Chat Mode | Conversational advising powered by Gemini AI |
|  Prolog Logic Mode | Deterministic prerequisite validation using Prolog |

---

#  AI Chat Mode

Implemented through `AIChatService`.

## Features

###  Conversational Advising

Uses Gemini (`gemini-2.5-flash`) to interact naturally with students.

---

###  Stateful Memory Management

Uses the **Singleton Design Pattern** via `ChatMemoryManager` to preserve conversation history across stateless HTTP requests.

---

###  Rule Supremacy & Conflict Resolution

The AI follows a strict **System Constitution** that prevents:

- Bypassing prerequisites
- Invalid academic paths
- Sycophantic behavior

Example:

> Even if a student insists on taking a course without prerequisites, the AI refuses based on university rules.

---

### ⚙ Functional Programming Techniques

Uses:

- `map()`
- `filter()`
- `lambda`

to securely process and clean chat history before sending it to:

- The frontend
- The Gemini API

---

#  Prolog Logic Mode

## Features

###  Deterministic Evaluation

Uses Prolog facts and rules to represent academic truths.

Example:

```prolog
prerequisite(programming2, programming1).
```

---

###  Constraint Satisfaction

Guarantees:

- Zero hallucinations
- Strict prerequisite validation
- Reliable eligibility checking

---

#  Architecture & Design Patterns

## Separation of Concerns (SoC)

The Django Views only handle:

- HTTP Requests
- HTTP Responses

All business logic is delegated to the services layer.

---

##   Singleton Pattern

Used in:

```python
ChatMemoryManager
```

Ensures only one shared memory instance exists during runtime.

---

##   Adapter / Wrapper Pattern

`AIChatService` acts as a wrapper around the Google GenAI SDK.

Responsibilities:

- Convert custom chat memory
- Translate messages into:

```python
genai.types.Content
```

---

##   Environment Security

Uses:

```python
python-dotenv
```

to securely load API keys into:

```python
os.environ
```

---

#  Getting Started

## Prerequisites

- Python 3.8+
- Linux / Unix environment (recommended)
- Virtual Environment (`venv`)

---

#   Installation

##  Create Virtual Environment

```bash
python3 -m venv .venv
```

---

## 2 Activate Environment

```bash
source .venv/bin/activate
```

---

## 3 Install Dependencies

```bash
python -m pip install django google-genai python-dotenv
```

Optional (for Prolog support):

```bash
pip install pyswip
```

---

## 4 Configure Environment Variables

Create a `.env` file beside `manage.py`

```env
GEMINI_API_KEY=your_secure_api_key_here
```

---

## 5 Run Development Server

```bash
python manage.py runserver
```

---

#  API Endpoints

# 1 AI Chat Endpoint

| Property | Value |
|---|---|
| URL | `/api/chat_with_ai_api/` |
| Method | `POST` |

## Description

- Receives student messages
- Updates server-side memory
- Sends request to Gemini
- Returns:
  - AI response
  - Filtered chat history

---

## Request Body

```json
{
    "message": "I finished programming 1, what should I take next?"
}
```

---

## Response

```json
{
    "status": "success",
    "response": "Congratulations on finishing Programming 1! Based on the university catalog...",
    "history": [
        {
            "role": "user",
            "content": "I finished programming 1, what should I take next?"
        },
        {
            "role": "model",
            "content": "Congratulations on finishing Programming 1!"
        }
    ]
}
```

---

# 2 Prolog Recommendation Endpoint

| Property | Value |
|---|---|
| URL | `/api/recommend/` |
| Method | `POST` |

## Description

Performs deterministic prerequisite evaluation using Prolog logic.

---

#  Security Notes

⚠ Never commit:

```bash
.env
```

Make sure it exists inside:

```bash
.gitignore
```

---

⚠ Always install dependencies inside the activated virtual environment.

---

#  Technologies Used

- Django
- Google Gemini API
- Prolog
- PySwip
- Python-dotenv

---

#  Academic Concepts Demonstrated

- Object-Oriented Programming (OOP)
- Functional Programming
- Logic Programming
- Prompt Engineering
- Design Patterns
- Constraint Satisfaction Systems

---

#  Authors

- Anas Mahmoud Abdullah
- Muhammad Jamal