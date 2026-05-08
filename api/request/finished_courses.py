
from typing import List
from .knowledge_base import Difficulty, Topic   # type: ignore
class FinishedCoursesManager:
    def __init__(self, courses: List[str] = None):
        self.courses = courses if courses else []
