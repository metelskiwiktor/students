package pl.wiktor.lessons.ui.panel.service;

import pl.wiktor.lessons.backend.model.Lesson;
import pl.wiktor.lessons.backend.service.LessonFacade;
import pl.wiktor.lessons.backend.model.Student;

import java.util.List;

public enum SpecificListPick {
    UNPAID_ALL_STUDENTS("Wszystkie nieopłacone lekcje"){
        @Override
        public List<Lesson> pickSpecificList(Student student, LessonFacade lessonFacade) {
            return lessonFacade.getUnpaidLessons();
        }
    },
    UNPAID_STUDENT("Wszystkie nieopłacone lekcje"){
        @Override
        public List<Lesson> pickSpecificList(Student student, LessonFacade lessonFacade) {
            return lessonFacade.getUnpaidLessons(student);
        }
    },
    ALL_LESSONS_STUDENT("Wszystkie lekcje"){
        @Override
        public List<Lesson> pickSpecificList(Student student, LessonFacade lessonFacade) {
            return lessonFacade.getLessons(student);
        }
    };

    private String description;

    SpecificListPick(String description) {
        this.description = description;
    }

    public abstract List<Lesson> pickSpecificList(Student student, LessonFacade lessonFacade);

    public String getDescription() {
        return description;
    }
}
