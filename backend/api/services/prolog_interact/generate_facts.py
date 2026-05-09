from api.knowledge.knowledge_base import COURSE_CATALOG, PREREQUISITE_GRAPH

class GenerateFacts:
 
 @staticmethod
 def generate_system_facts() -> str:
        facts = []
        
        for course in COURSE_CATALOG.keys():
            facts.append(f"course('{course}').")
            
        for course, details in COURSE_CATALOG.items():
            facts.append(f"course_difficulty('{course}', {details['difficulty']}).")
            
        for course, details in COURSE_CATALOG.items():
            facts.append(f"course_topic('{course}', '{details['topic']}').")

        for target_course, prerequisites in PREREQUISITE_GRAPH.items():
            for prereq in prerequisites:
                facts.append(f"prerequest('{prereq}', '{target_course}').")

        courses_with_prereqs = PREREQUISITE_GRAPH.keys()
        for course in COURSE_CATALOG.keys():
            if course not in courses_with_prereqs:
                facts.append(f"not_have_prerequest('{course}').")

        return "\n".join(facts)