from django.shortcuts import render

import json
from django.http import JsonResponse
from django.views.decorators.csrf import csrf_exempt
from .prolog_service import get_prolog_recommendation
@csrf_exempt
def recommend_course(request):
    if request.method == 'POST':
        try:
            data = json.loads(request.body)
            
            chosen_mode = data.get('mode')
            
            if chosen_mode == 'prolog':
                #we connect the backend with prolog 
                result  = get_prolog_recommendation(data)
            elif chosen_mode == 'ai':
                result = "Data Structures (from AI)"
            else:
                result = "Error: Unknown mode"

            return JsonResponse({"recommended_course": result, "status": "success"})
            
        except json.JSONDecodeError:
            return JsonResponse({"error": "Invalid JSON format"}, status=400)
            
    return JsonResponse({"error": "Only POST method is allowed"}, status=405)