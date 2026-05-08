import json

from api.request_response.user_request import UserRequest
from api.services.ai_interact.knowledge_promp_generator import KnowledgePromptGenerator



class AIService:
    @staticmethod
    def build_complete_prompt(user_request: 'UserRequest') -> str:
        user_context = user_request.generate_user_prompt()
        
        knowledge_context = KnowledgePromptGenerator.generate_knowledge_prompt()
        """
        we tell AI here how must response must be to make it like our 
        reponse object
        """
        format_instruction = (
            "\n\nResponse Format: You MUST respond in valid JSON only.\n"
            "JSON structure: "
            '{"status": "success", "recommendations": [{"course": "name", "reason": "why"}], "message": "summary"}'
        )
        
        return f"{knowledge_context}\n\n{user_context}\n\n{format_instruction}"

    # @staticmethod
    # def send_to_ai(final_prompt: str) -> 'AIRecommendationResponse':
    #     #implement here the real request to AI MODEL Gama 