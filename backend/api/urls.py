# from django.urls import path
from django.urls import path

from . import views
urlpatterns = [
path('recommend', views.recommend_course_api, name='recommend_course_api'),
    path('get_metadata_api/', views.get_metadata_api,name='get_metadate_api'),
]