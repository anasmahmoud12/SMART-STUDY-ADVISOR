import subprocess
import os
from pyswip import Prolog
from api.models import Course

class StudentRequest:
    def __init__(self, name, difficulty, interest):
        self.name = name
        self.difficulty = difficulty
        self.interest = interest
        
    def to_prolog_facts(self):
        return [f"student_preference('{self.name}', {self.difficulty})", f"student_interest('{self.name}', {Course.objects.filter(topic_display_name=self.interest).first().topic})"]

def get_prolog_recommendation(data_dict):
    clean_text = lambda x: str(x).lower().strip()
    cleaned_data = {k: clean_text(v) for k, v in data_dict.items()}
    
    student = StudentRequest(
        name=cleaned_data.get('student_name', 'student_1'),
        difficulty=cleaned_data.get('difficulty'),
        interest=cleaned_data.get('interest')
    )

    try:
        base_dir = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
        prolog_file = os.path.join(base_dir, 'logic_engine', 'rules.pl')

        student_facts = student.to_prolog_facts()
        query = f"recommend('{student.name}', Course)."
        
        process = subprocess.Popen(
            ['swipl', '-s', prolog_file, '-g', f"assert({student_facts.split('.')[0]}), assert({student_facts.split('.')[1]}), {query}", '-t', 'halt'],
            stdout=subprocess.PIPE,
            stderr=subprocess.PIPE,
            text=True
        )
        
        stdout, stderr = process.communicate()
        
        if "Course =" in stdout:
            course = stdout.split("Course =")[1].strip()
            return f"Prolog Recommends: {course}"
        else:
            return "Prolog could not find a matching course."

    except Exception as e:
        return f"Error connecting to Prolog: {str(e)}"


def testpyswip():
    prolog = Prolog()
    base_dir = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
    # print(base_dir)
    prolog_file = os.path.join(base_dir, '..\\', 'logic_engine', 'rules.pl')
    print(prolog_file)



    prolog.consult(prolog_file)

    facts = [

        "student_preference(anas, easy)",
        "student_preference(anas, medium)",

        "(anas, ai)",
        "student_interest(anas, cybersecurity)",
        "student_interest(anas, software_engineering)",

        "course(programming1)",
        "course(machine_learning)",
        "course(network_security)",

        "course_difficulty(programming1, easy)",
        "course_difficulty(machine_learning, medium)",
        "course_difficulty(network_security, hard)",

        "course_topic(programming1, software_engineering)",
        "course_topic(machine_learning, ai)",
        "course_topic(network_security, cybersecurity)"
    ]

    for fact in facts:
        prolog.assertz(fact)

    results = list(
        prolog.query("recommend(anas, Course)")
    )
    print(results)
    return ""