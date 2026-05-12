


from api.knowledge.knowledge_base import get_prerequiste_graph
from api.models import Course


class KnowledgePromptGenerator:
    @staticmethod
    def generate_knowledge_context() -> str:
      
        context = "ACADEMIC RULES & CONSTRAINTS (STRICT ENFORCEMENT):\n\n"
        courses = Course.objects.all()
        
        context += "1. AVAILABLE COURSE CATALOG:\n"
        for course in courses:
            context += f"- {course.display_name}: Difficulty: {course.difficulty}, Topic: {course.topic_display_name}\n"

        context += "\n2. PREREQUISITES (DEPENDENCY GRAPH):\n"
        prereqs = get_prerequiste_graph()
        for course in prereqs.keys():
            context += f"- To unlock '{course}', the student MUST have completed: {', '.join(prereqs[course])}\n"

        context += (
            "\n3. MANDATORY OPERATIONAL RULES:\n"
            "- NEVER suggest a course the student has already finished.\n"
            "- NEVER suggest a course if the prerequisites are not met based on the provided list.\n"
            "- You must prioritize courses that match the student's interests and preferred difficulty.\n"
            "- If no courses fit the criteria, return an empty list.\n"
        )
        return context