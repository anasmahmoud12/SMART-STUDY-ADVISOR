from api.knowledge.knowledge_base import get_prerequiste_graph

from typing import List,Tuple
class FinishedCoursesManager:
    def __init__(self, courses: List[str] = None):
        self.courses = courses if courses else []
    
    def validate(self) -> Tuple[bool, List[str], List[str]]:
        valid_courses = []
        invalid_courses = []

        prerequisite_graph = get_prerequiste_graph()
        for course in self.courses:
            if course in prerequisite_graph and prerequisite_graph[course] != []:
                prereqs = prerequisite_graph[course]
                has_all_prereqs = all(p in self.courses for p in prereqs)
                
                if has_all_prereqs:
                    valid_courses.append(course)
                else:
                    invalid_courses.append(course)
            else:
                valid_courses.append(course)
                
        is_valid = len(invalid_courses) == 0 
        
        return is_valid, valid_courses, invalid_courses