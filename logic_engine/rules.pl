course(cs101)
course(cs201)
course(cs301)

course_difficulty(cs101, easy).
course_difficulty(cs201, medium).
course_difficulty(cs301, hard).

course_topic(cs101, programming).
course_topic(cs201, software_engineering).
course_topic(cs301, ai).


recommend(Student, Course) :-
    student_preference(Student, Difficulty),
    student_interest(Student, Topic),
    course_difficulty(Course, Difficulty),
    course_topic(Course, Topic).