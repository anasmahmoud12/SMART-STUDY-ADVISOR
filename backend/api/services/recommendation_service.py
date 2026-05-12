# api/general_service.py
from typing import Union

from api.knowledge.enums.difficulty import Difficulty
from api.knowledge.enums.topic import Topic
from api.request_response.recommendation_response import RecommendationResponse
from api.request_response.error_response import ErrorResponse


from api.request_response.system_meta_data_response import SystemMetadataResponse
from api.request_response.user_request import UserRequest
from api.request_response.validator.finished_courses import FinishedCoursesManager
from api.services.prolog_interact.prolog_inference_service import PrologInferenceService
from api.models import Course

class RecommendationService:
    
    @staticmethod
    def process_prolog_request(data_dict: dict) -> Union[RecommendationResponse, ErrorResponse]:
        raw_finished_courses = data_dict.get('finished_courses', [])
        finished_courses = []

        for course_name in raw_finished_courses:
            finished_courses.append(Course.objects.get(display_name=course_name).name)
        
        validator = FinishedCoursesManager(raw_finished_courses)
        is_valid, valid_list, invalid_list = validator.validate()
        
        if not is_valid:
            return ErrorResponse(
                message="Validation Failed: Missing prerequisites.",
                invalid_items=invalid_list
            )
        
        difficulty = data_dict.get('difficulty', 'elementary')
        interests = data_dict.get('interests', [])
        
        clean_user_request = UserRequest(
            name=data_dict.get('student_name', 'student'),
            difficulty=difficulty,
            finished_courses=valid_list, 
            interests=interests
        )
        
       
        final_response = PrologInferenceService.getPyswipRecommendation(clean_user_request)
        
        return final_response
    
    @staticmethod
    def get_system_metadata_new() -> SystemMetadataResponse:
        courses = Course.objects.all()
        course_names = []
        topics = []
        difficulties = ['elementary', 'intermediate', 'advanced']

        for course in courses:
            if course.display_name not in course_names: course_names.append(course.display_name)
            if course.topic_display_name not in topics: topics.append(course.topic_display_name)
            
        response_obj = SystemMetadataResponse(
            courses=course_names,
            difficulties=difficulties,
            topics=topics
        )

        return response_obj 
    
