from typing import List

from api.knowledge.enums.difficulty import Difficulty
from api.knowledge.enums.topic import Topic


class UserRequest:
    def __init__(self, name: str, difficulty: Difficulty, finished_courses: List[str], interests: List[Topic]):
        self.name = name
        
        self.difficulty = difficulty
        
        self.finished_courses = finished_courses 
        
        self.interests = interests

    def to_prolog_facts(self):
       
        facts = []
        
        facts.append(f"student_preference('{self.name}', '{self.difficulty.value}').")
        
        for interest in self.interests:
            facts.append(f"student_interest('{self.name}', '{interest.value}').")
            
        for course in self.finished_courses:
            facts.append(f"has_finished('{self.name}', '{course}').")
            
        return "\n".join(facts) 
    """

    """
    def generate_user_context(self) -> str:
       
        interests_str = ", ".join([i.value for i in self.interests])
        finished_str = ", ".join(self.finished_courses) if self.finished_courses else "None"
        
        prompt = (
            f"Current Student Profile for {self.name}:\n"
            f"- Already Completed Courses: [{finished_str}]\n"
            f"- Academic Interests: [{interests_str}]\n"
            f"- Preferred Difficulty Level: {self.difficulty.value}\n"
        )
        return prompt