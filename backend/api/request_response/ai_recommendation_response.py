"""
the only change we will make AI make new thing 
return courses + why he choose this one 

as show adv of AI above normal prolog

"""


from typing import List


class AIRecommendationResponse:
    def __init__(self, status: str, recommendations: List[dict], message: str = ""):
      
        self.status = status
        self.recommendations = recommendations
        self.message = message

    def to_dict(self) -> dict:
        
        return {
            "status": self.status,
            "message": self.message,
            "data": {
                "recommendations": self.recommendations
            }
        }