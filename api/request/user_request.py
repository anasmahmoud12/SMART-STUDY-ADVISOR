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

    def to_prolog_facts(self):
       
        facts = []
        
        facts.append(f"student_preference('{self.name}', {self.difficulty.value}).")
        
        for interest in self.interests:
            facts.append(f"student_interest('{self.name}', {interest.value}).")
            
        for course in self.finished_courses.courses:
            facts.append(f"has_finished('{self.name}', {course}).")
            
        return "\n".join(facts)