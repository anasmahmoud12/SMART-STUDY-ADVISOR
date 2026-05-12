import os
from pathlib import Path
from dotenv import load_dotenv
from google import genai
from api.services.ai_chat_box_mode.chat_memory_manegar import ChatMemoryManager
from dotenv import load_dotenv

key_path = Path(__file__).resolve().parent.parent.parent.parent / ".env"
load_dotenv(key_path)
api_key_string = os.getenv("GEMINI_API_KEY")
client = genai.Client(api_key=api_key_string)

class AIChatService:
    def __init__(self):
        # singletone object
        self.memory_manager = ChatMemoryManager()

    def process_user_input(self, user_message: str) -> None:
        current_history = self.memory_manager.get_history()
        # print(current_history)
        
        if len(current_history) == 1:
            print("--- First Request: System history is already created. Adding user message. ---")
            self.memory_manager.add_message(role="user", message=user_message)
        else:
            print("--- Subsequent Request: Appending to existing history matrix. ---")
            self.memory_manager.add_message(role="user", message=user_message)

    def fetch_ai_response(self) -> str:
        raw_history = self.memory_manager.get_history()
        system_instruction = ""
        gemini_formatted_history = []

        for message in raw_history:
            if message["role"] == "system":
                system_instruction = message["content"]
            else:
                gemini_formatted_history.append(
                    genai.types.Content(
                        role=message["role"],
                        parts=[genai.types.Part(text=message["content"])]
                    )
                )

        try:
            response = client.models.generate_content(
                model="gemini-2.5-flash", 
                contents=gemini_formatted_history,
                config=genai.types.GenerateContentConfig(
                    system_instruction=system_instruction
                )
            )
            return response.text
        except Exception as e:
            raise ConnectionError(f"Gemini API failed to respond: {str(e)}")
            
    def process_ai_output(self, ai_response: str) -> None:
        self.memory_manager.add_message(role="model", message=ai_response)
    def get_chat_history_for_frontend(self) -> list:
      
        raw_history = self.memory_manager.get_history()
        filtered_history = []
        
        for message in raw_history:
            if message["role"] != "system":
                filtered_history.append(message)
                
        return filtered_history       
    def clear_hist(self, ai_response: str) -> None:
        self.memory_manager.clear_history()