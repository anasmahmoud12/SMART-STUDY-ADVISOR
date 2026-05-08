import subprocess
import os
import tempfile
from typing import List

from api.services.prolog_interact.generate_facts import GenerateFacts
from api.request_response.user_request import UserRequest 

class PrologInferenceService:
    
    @staticmethod
    def get_recommendation(user_request: UserRequest) -> RecommendationResponse:
        
        try:
            system_facts = GenerateFacts.generate_system_facts()
            
            user_facts = user_request.to_prolog_facts()
            
            all_facts = f"{system_facts}\n{user_facts}"
            #with each request there is file we create it to put facts 
            """
            so request if it many in same time each one access on each file
            by tell OS make file by random name and open to write and make it .pl
            and not close it i will explict close it
            this file to put in it facts  
            """
            with tempfile.NamedTemporaryFile(mode='w', suffix='.pl', delete=False) as temp_file:
                #write facts 
                temp_file.write(all_facts)
                #take the name of file 
                temp_facts_path = temp_file.name
            #we make that not directly put the address as it can run in other machine not have the same path 
            #we need absolute path
            """
            what code make here __file__ is object have the name of file of our code 
            we take it and then return it to absolute by abspath
            then tell him go back one step dirname 
            then other one step and one another 
           
            
             why he make that to make the code not relative and depand on 
            where we in terminal and make run to python code
            """ 
            base_dir = os.path.dirname(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))
            rules_file = os.path.join(base_dir, 'logic_engine', 'rules.pl')

            #queury recommand but it is called find all 
            """
            which tell prolog return all courses not one by one 
            """
            query = f"findall(Course, recommend('{user_request.name}', Course), CoursesList), writeln(CoursesList)."
            """
            this make child process but 
            this in simple way  tell OS can you open terminal and run the command 
             ['swipl', '-s', rules_file, '-s', temp_facts_path, '-g', query, '-t', 'halt'],
             and make pipe to not print output in terminal print but i want it 
             and stderr this pip if there is error 

            """
            process = subprocess.Popen(
                ['swipl', '-s', rules_file, '-s', temp_facts_path, '-g', query, '-t', 'halt'],
                stdout=subprocess.PIPE,
                stderr=subprocess.PIPE,
                text=True
            )
            """
            this to tell python wait prolog finish and after he finish 
            continue 
            """
            
            stdout, stderr = process.communicate()
            print(stdout)
            #this remove our facts file this was just temporary file of facts 
            os.remove(temp_facts_path)
            """
            parsing the text which come from prolog
            
            """
            # remove all spaces 
            raw_output=stdout.strip()
            recommended_list = []
            if raw_output == "[]" or not raw_output.startswith("["):
              
              return RecommendationResponse(
                status="success",
                message="No courses match your current criteria.",
                recommended_courses=[]
                        )
            else:
               clean_text = raw_output.replace("[", "").replace("]", "").strip()
    
               recommended_list = clean_text.split(",")
    
               recommended_list = [course.strip() for course in recommended_list if course.strip()]
    
               return RecommendationResponse(
        status="success",
        message="Recommendations generated successfully.",
        recommended_courses=recommended_list
    )

        except Exception as e:
            if 'temp_facts_path' in locals() and os.path.exists(temp_facts_path):
                os.remove(temp_facts_path)
            
            return RecommendationResponse(
                status="error",
                message=f"System failed to process logic: {str(e)}",
                recommended_courses=[]
            )