from typing import List

from api.knowledge.enums.difficulty import Difficulty
from api.knowledge.enums.topic import Topic
from api.models import Course


class UserRequest:
    def __init__(self, name: str, difficulty: str, finished_courses: List[str], interests: List[str]):
        self.name = name
        
        self.difficulty = difficulty
        
        self.finished_courses = finished_courses 
        
        self.interests = interests

    def to_prolog_facts(self):
       
        facts = []
        
        facts.append(f"student_preference({self.name}, {self.difficulty})")
        
        for interest in self.interests:
            facts.append(f"student_interest({self.name}, {Course.objects.filter(topic_display_name=interest).first().topic})")
            
        for course in self.finished_courses:
            facts.append(f"has_finished({self.name}, {Course.objects.filter(display_name=course).first().name})")
            
        return "\n".join(facts) 
    """

    """
    def generate_user_context(self) -> str:
       
        interests_str = ", ".join([i for i in self.interests])
        finished_str = ", ".join(self.finished_courses) if self.finished_courses else "None"
        
        prompt = (
            f"Current Student Profile for {self.name}:\n"
            f"- Already Completed Courses: [{finished_str}]\n"
            f"- Academic Interests: [{interests_str}]\n"
            f"- Preferred Difficulty Level: {self.difficulty.value}\n"
        )
        return prompt