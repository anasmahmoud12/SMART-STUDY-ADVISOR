import subprocess
import os
import tempfile
from typing import List

from api.models import UserRequest, RecommendationResponse
from api.services.prolog_interact.generate_facts import GenerateFacts 

class PrologInferenceService:
    
    @staticmethod
    def get_recommendation(user_request: UserRequest) -> RecommendationResponse:
        
        try:
            system_facts = GenerateFacts.generate_system_facts()
            
            user_facts = user_request.to_prolog_facts()
            
            all_facts = f"{system_facts}\n{user_facts}"
            
            with tempfile.NamedTemporaryFile(mode='w', suffix='.pl', delete=False) as temp_file:
                temp_file.write(all_facts)
                temp_facts_path = temp_file.name

            base_dir = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
            rules_file = os.path.join(base_dir, 'logic_engine', 'rules.pl')

            query = f"recommend('{user_request.name}', Course)."

            process = subprocess.Popen(
                ['swipl', '-s', rules_file, '-s', temp_facts_path, '-g', query, '-t', 'halt'],
                stdout=subprocess.PIPE,
                stderr=subprocess.PIPE,
                text=True
            )
            
            stdout, stderr = process.communicate()
            
            os.remove(temp_facts_path)
            
            recommended_list = []
            if "Course =" in stdout:
                course = stdout.split("Course =")[1].strip()
                recommended_list.append(course)
                
                return RecommendationResponse(
                    status="success",
                    message="Recommendation generated successfully.",
                    recommended_courses=recommended_list
                )
            else:
                return RecommendationResponse(
                    status="success",
                    message="No courses match your current criteria.",
                    recommended_courses=[]
                )

        except Exception as e:
            if 'temp_facts_path' in locals() and os.path.exists(temp_facts_path):
                os.remove(temp_facts_path)
            
            return RecommendationResponse(
                status="error",
                message=f"System failed to process logic: {str(e)}",
                recommended_courses=[]
            )