package pl.wiktor.lessons.backend.service;

import pl.wiktor.lessons.backend.http.HttpClientServiceLesson;
import pl.wiktor.lessons.backend.model.Lesson;
import pl.wiktor.lessons.backend.service.filter.LessonFilters;

import java.util.List;

class LessonService {
    private HttpClientServiceLesson httpClientServiceLesson;
    private List<Lesson> lessons;
    private LessonFilters lessonFilters;

    LessonService() {
        this.httpClientServiceLesson = new HttpClientServiceLesson();
        lessons = httpClientServiceLesson.getAll();
        lessonFilters = new LessonFilters(lessons);
    }

    void addLesson(Lesson lesson) {
        lessons = httpClientServiceLesson.add(lesson);
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    LessonFilters getLessonFilters(){
        lessonFilters.setLessons(lessons);
        return lessonFilters;
    }

    void deleteLesson(Lesson lesson) {
        lessons = httpClientServiceLesson.delete(lesson);
    }

    void editLesson(Lesson lesson) {
        lessons = httpClientServiceLesson.update(lesson);
    }
}
