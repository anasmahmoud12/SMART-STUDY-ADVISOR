import csv
from api.models import Course
from django.core.management.base import BaseCommand

class Command(BaseCommand):
    help = "Load csv into database"

    def handle(self, *args, **kwargs):
        load_csv()
        self.stdout.write(self.style.SUCCESS("csv loaded successfully"))

def load_csv():
    with open('courses.csv', newline="", encoding="utf-8") as file:

        reader = csv.DictReader(file)

        courses = {}
        for row in reader:
            course = Course.objects.create(
                display_name=row["display_name"],
                name=row["name"],
                topic=row["topic"],
                topic_display_name=row["topic_display_name"],
                difficulty=row["difficulty"]
            )
            course.save()
            courses[row["name"]] = course

        for row in reader:
            for pre in row["prerequisites"].split("|"):
                course = courses[row["name"]]
                prerequisite = courses[pre.strip()]
                course.prerequisites.add(pre)