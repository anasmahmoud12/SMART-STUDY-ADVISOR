%%
input from user is his name 
his course he take 
level of course_difficulty he want  this not rule 
what course he student_interest
but other three ones will be facts i will add in file 

facts we will add 

% student(anas).

% student_interest(anas,machine_langauge).


% has_finish(anas,programming1).

 



%%

course(ai).
course(programming1).
course(software_engineering).
course(programming2).
course(machine_langauge).

course_difficulty(machine_langauge,medium).
course_difficulty(ai, hard).
course_difficulty(software_engineering, medium).
course_difficulty(programming1, easy).
course_difficulty(programming2,hard).



prerequest(machine_langauge,ai).
prerequest(programming1,programming2).
prerequest(programming1,software_engineering).

not_have_prerequest(programming1).
not_have_prerequest(machine_langauge).

% this will be injected from user    
has_finish(Student,Course).
%%%%%%%%%%%%%%%%%%%%%%%%% if there is thing 


student_learn(Student,Course) :- 
   has_finish(Student,Course).


student_learn(Student,Course):-
    prerequest(Course,Z),
    student_learn(Student,Z).
    

recommend(Student, Course,UserDifficulty) :-
    student(Student),
    course(Course),
    course_difficulty(Course,UserDifficulty),
    student_interest(Student,Course),
   (prerequest(Z, Course) -> student_learn(Student, Z) ; not_have_prerequest(Course)).
