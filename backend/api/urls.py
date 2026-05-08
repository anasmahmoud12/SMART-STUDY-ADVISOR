# from django.urls import path
from django.urls import path

from . import views

urlpatterns = [
path('recommend', views.recommend_course_api, name='recommend_course_api'),
]