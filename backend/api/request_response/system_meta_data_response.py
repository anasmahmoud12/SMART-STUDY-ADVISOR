
class SystemMetadataResponse:
    def __init__(self, courses: list, difficulties: list, topics: list):
        self.status = "success"
        self.courses = courses
        self.difficulties = difficulties
        self.topics = topics

    def to_dict(self):
       
        return {
            "status": self.status,
            "data": {
                "courses": self.courses,
                "difficulties": self.difficulties,
                "topics": self.topics
            }
        }