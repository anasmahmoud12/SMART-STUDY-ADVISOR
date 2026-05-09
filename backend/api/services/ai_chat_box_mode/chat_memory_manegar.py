from typing import List, Dict
from api.knowledge.knowledge_base import COURSE_CATALOG, PREREQUISITE_GRAPH
from api.knowledge.enums.difficulty import Difficulty
from api.knowledge.enums.topic import Topic

class ChatMemoryManager:
    _instance = None

    def __new__(cls, *args, **kwargs):
        if cls._instance is None:
            cls._instance = super(ChatMemoryManager, cls).__new__(cls)
            cls._instance._initialize_system_history()
        return cls._instance

    def _initialize_system_history(self):
       
        catalog_info = "AVAILABLE COURSES CATALOG:\n"
        for course, details in COURSE_CATALOG.items():
            catalog_info += f"- '{course}': Difficulty: {details['difficulty']}, Topic: {details['topic']}\n"

        prereq_info = "\nPREREQUISITE RULES:\n"
        for course, prereqs in PREREQUISITE_GRAPH.items():
            prereq_info += f"- To study '{course}', MUST finish: {', '.join(prereqs)}\n"

        system_guidelines = (
            "You are an Academic Advisor AI. Follow these rules STRICTLY:\n\n"
            f"{catalog_info}\n"
            f"{prereq_info}\n"
            "CRITICAL INSTRUCTIONS:\n"
            "1. SEMANTIC MATCHING: The user might write course names informally (e.g., 'programming one' or 'prog 1'). "
            "You MUST understand they mean 'programming1' from the catalog.\n"
            "2. Never recommend a course if its prerequisites are not met.\n"
            "3. Be conversational, friendly, and ask clarifying questions if needed."
        )

        self.chat_history: List[Dict[str, str]] = [
            {"role": "system", "content": system_guidelines}
        ]
        print(self.chat_history)

    def add_message(self, role: str, message: str) -> None:
      
        if role not in ["user", "model", "system"]:
            raise ValueError("Role must be 'user', 'model', or 'system'")
            
        new_entry = {
            "role": role,
            "content": message
        }
        self.chat_history.append(new_entry)

    def get_history(self) -> List[Dict[str, str]]:
       
        return self.chat_history

    def clear_history(self) -> None:
       
        self._initialize_system_history()