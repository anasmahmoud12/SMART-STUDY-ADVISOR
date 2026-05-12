from django.db import models

# Create your models here.

class Course(models.Model):
    display_name = models.CharField(max_length=32)
    name = models.CharField(max_length=32)
    topic = models.CharField(max_length=32)
    topic_display_name = models.CharField(max_length=32)
    difficulty = models.CharField(max_length=32)

    prerequisites = models.ManyToManyField(
        "self",
        symmetrical=False,
        blank=True
    )
