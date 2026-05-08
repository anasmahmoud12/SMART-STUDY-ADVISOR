# api/general_service.py
from typing import Union

from api.knowledge.enums.difficulty import Difficulty
from api.knowledge.enums.topic import Topic
from api.request_response.RecommendationResponse import RecommendationResponse
from api.request_response.ErrorResponse import ErrorResponse
from backend.api.knowledge.knowledge_base import COURSE_CATALOG
from backend.api.request_response.system_meta_data_response import SystemMetadataResponse
from backend.api.request_response.user_request import UserRequest
from backend.api.request_response.validator.finished_courses import FinishedCoursesManager
from backend.api.services.prolog_interact.prolog_inference_service import PrologInferenceService

class RecommendationService:
    
    @staticmethod
    def process_prolog_request(data_dict: dict) -> Union[RecommendationResponse, ErrorResponse]:
        raw_finished_courses = data_dict.get('finished_courses', [])
        
        validator = FinishedCoursesManager(raw_finished_courses)
        is_valid, valid_list, invalid_list = validator.validate()
        
        if not is_valid:
            return ErrorResponse(
                message="Validation Failed: Missing prerequisites.",
                invalid_items=invalid_list
            )
        
        difficulty_enum = Difficulty(data_dict.get('difficulty', 'easy'))
        interests_enums = [Topic(i) for i in data_dict.get('interests', [])]
        
        clean_user_request = UserRequest(
            name=data_dict.get('student_name', 'student'),
            difficulty=difficulty_enum,
            finished_courses=valid_list, 
            interests=interests_enums
        )
        
       
        final_response = PrologInferenceService.get_recommendation(clean_user_request)
        
        return final_response
    
    @staticmethod
    def get_system_metadata() -> SystemMetadataResponse:
        
        available_courses = list(COURSE_CATALOG.keys())
        difficulties = [d.value for d in Difficulty]
        topics = [t.value for t in Topic]
        
        response_obj = SystemMetadataResponse(
            courses=available_courses,
            difficulties=difficulties,
            topics=topics
        )
        
        return response_obj