
check_prereq(_Student, Course) :-
    not_have_prerequest(Course).

check_prereq(Student, Course) :-
    \+ not_have_prerequest(Course),
    forall(prerequest(Pre, Course), has_finished(Student, Pre)).



recommend(Student, Course) :-
    course(Course),

    student_preference(Student, TargetDiff),
    course_difficulty(Course, TargetDiff),

    student_interest(Student, TargetTopic),
    course_topic(Course, TargetTopic),

    \+ has_finished(Student, Course),

    check_prereq(Student, Course).