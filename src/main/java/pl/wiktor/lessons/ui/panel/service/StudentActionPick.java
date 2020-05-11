package pl.wiktor.lessons.ui.panel.service;

import pl.wiktor.lessons.backend.service.LessonFacade;
import pl.wiktor.lessons.backend.model.Student;

public enum StudentActionPick {
    ADD("Dodaj ucznia"){
        @Override
        public void makeAction(LessonFacade lessonFacade, Student student) {
            lessonFacade.createStudent(student);
        }
    },
    EDIT("Zedytuj"){
        @Override
        public void makeAction(LessonFacade lessonFacade, Student student) {
            lessonFacade.editStudent(student);
        }
    };

    private String description;

    StudentActionPick(String description) {
        this.description = description;
    }

    public abstract void makeAction(LessonFacade lessonFacade, Student student);

    public String getDescription() {
        return description;
    }
}
