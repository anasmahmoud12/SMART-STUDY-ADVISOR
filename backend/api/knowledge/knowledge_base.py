from api.knowledge.enums.difficulty import Difficulty
from api.knowledge.enums.topic import Topic

COURSE_CATALOG = {
    "programming1": {"difficulty": Difficulty.EASY.value, "topic": Topic.PROGRAMMING.value},
    "software_engineering": {"difficulty": Difficulty.MEDIUM.value, "topic": Topic.SOFTWARE_ENGINEERING.value},
    "machine_language": {"difficulty": Difficulty.MEDIUM.value, "topic": Topic.PROGRAMMING.value},
    "ai": {"difficulty": Difficulty.HARD.value, "topic": Topic.AI.value},
    "programming2": {"difficulty": Difficulty.HARD.value, "topic": Topic.PROGRAMMING.value}
}

PREREQUISITE_GRAPH = {
    "ai": ["machine_language"],                 
    "programming2": ["programming1"],           
    "software_engineering": ["programming1"]   
}