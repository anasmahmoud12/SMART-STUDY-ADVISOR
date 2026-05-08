from typing import List

from api.knowledge.enums.difficulty import Difficulty
from api.knowledge.enums.topic import Topic
from api.request.finished_courses import FinishedCoursesManager


class UserRequest:
    def __init__(self, name: str, difficulty: Difficulty, finished_manager: FinishedCoursesManager, interests: List[Topic]):
        self.name = name
        
        self.difficulty = difficulty
        
        self.finished_courses = finished_manager 
        
        self.interests = interests

 