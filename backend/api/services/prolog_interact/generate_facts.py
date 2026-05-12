from api.models import Course

class GenerateFacts:
 
 @staticmethod
 def generate_system_facts() -> str:
        facts = []
        courses = Course.objects.all()

        for course in courses:
            facts.append(f"course({course.name})")
            
        for course in courses:
            facts.append(f"course_difficulty({course.name}, {course.difficulty})")
            
        for course in courses:
            facts.append(f"course_topic({course.name}, {course.topic})")

        for course in courses:
            prerequisites = course.prerequisites.all()
            if prerequisites == []:
                facts.append(f"not_have_prerequest({course.name})")
                continue

            for prereq in prerequisites:
                facts.append(f"prerequest({prereq.name}, {course.name})")


        return "\n".join(facts)