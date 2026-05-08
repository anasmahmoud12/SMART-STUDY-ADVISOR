from typing import List

class RecommendationResponse:
    def __init__(self, status: str, recommended_courses: List[str], message: str = ""):
        self.status = status
        self.recommended_courses = recommended_courses
        self.message = message

    def to_dict(self):
        return {
            "status": self.status,
            "message": self.message,
            "data": {
                "courses": self.recommended_courses
            }
        }