from django.shortcuts import render

import json
from django.http import JsonResponse
from django.views.decorators.csrf import csrf_exempt
import tempfile
import os
from django.http import JsonResponse
from django.views.decorators.csrf import csrf_exempt
from api.request_response.error_response import ErrorResponse
from api.services.recommendation_service import RecommendationService
from api.services.ai_chat_box_mode.ai_chat_service import AIChatService
from .prolog_service import get_prolog_recommendation

"""
example on the json must send from frontend which have the input of 
user
{
    "student_name": "anas",
    "difficulty": "medium",
    "interests": ["ai", "software_engineering"],
    "finished_courses": ["programming1", "machine_language"]
}
"""
# http://127.0.0.1:8000/api/recommend

@csrf_exempt
def recommend_course_api(request):
    if request.method == 'POST':
        try:
            data_dict = json.loads(request.body)
            
            service_result = RecommendationService.process_prolog_request(data_dict)
            
            if isinstance(service_result, ErrorResponse):
                status_code = 400 
            else:
                status_code = 200 
                
            return JsonResponse(service_result.to_dict(), status=status_code)
            
        except json.JSONDecodeError:
            return JsonResponse({"error": "Invalid JSON format"}, status=400)
        except ValueError as e:
            return JsonResponse({"error": f"Invalid data value: {str(e)}"}, status=400)

"""



                 "courses":[],
                "difficulties":[],
                "topics":   []


"""
# http://127.0.0.1:8000/api/get_metadata_api/ 

def get_metadata_api(request):
    if request.method == 'GET':

        metadata_obj = RecommendationService.get_system_metadata()
        
        return JsonResponse(metadata_obj.to_dict())   
# http://127.0.0.1:8000/api/chat_with_ai_api/
@csrf_exempt
def chat_with_ai_api(request):
    if request.method == 'POST':
        try:
            data_dict = json.loads(request.body)
            user_text = data_dict.get("message")

            ai_service = AIChatService()
            ai_service.process_user_input(user_text)
            ai_reply = ai_service.fetch_ai_response()
            ai_service.process_ai_output(ai_reply)

            history_matrix = ai_service.get_chat_history_for_frontend()

            return JsonResponse({"status": "success",
                                  "response": ai_reply,
                                  "history": history_matrix})

        except json.JSONDecodeError:
            return JsonResponse({"error": "Invalid JSON format"}, status=400)
        except Exception as e:
            return JsonResponse({"status": "error", "message": str(e)}, status=500)

@csrf_exempt
def chat_with_voice_api(request):
    if request.method == 'POST':
        try:
            audio_file = request.FILES.get('audio')
            
            if not audio_file:
                return JsonResponse({"error": "No audio file provided. Please send a file with the key 'audio'"}, status=400)

            with tempfile.NamedTemporaryFile(delete=False, suffix='.m4a') as temp_audio:
                for chunk in audio_file.chunks():
                    temp_audio.write(chunk)
                
                temp_file_path = temp_audio.name
            
            os.remove(temp_file_path)

            return JsonResponse({
                "status": "success", 
                "message": "Audio received successfully!",
                "saved_at": temp_file_path
            })

        except Exception as e:
            return JsonResponse({"status": "error", "message": str(e)}, status=500)     