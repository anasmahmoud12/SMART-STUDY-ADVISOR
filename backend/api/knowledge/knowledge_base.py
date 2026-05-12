from api.knowledge.enums.difficulty import Difficulty
from api.knowledge.enums.topic import Topic
from api.models import Course

def get_prerequiste_graph():
    courses = Course.objects.all()
    prerequisite_graph = {}

    for course in courses:
        prerequisite_graph[course.name] = course.prerequisites.values_list("name", flat=True)

    return prerequisite_graph
