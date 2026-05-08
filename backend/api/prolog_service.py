import subprocess
import os

class StudentRequest:
    def __init__(self, name, difficulty, interest):
        self.name = name
        self.difficulty = difficulty
        self.interest = interest
        
    def to_prolog_facts(self):
        return f"student_preference('{self.name}', {self.difficulty}). student_interest('{self.name}', {self.interest})."

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