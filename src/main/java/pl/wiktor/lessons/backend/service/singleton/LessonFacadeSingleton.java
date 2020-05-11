package pl.wiktor.lessons.backend.service.singleton;

import pl.wiktor.lessons.backend.service.LessonFacade;

public enum LessonFacadeSingleton {
    LESSON_FACADE(new LessonFacade());

    private LessonFacade lessonFacade;

    LessonFacadeSingleton(LessonFacade lessonFacade) {
        this.lessonFacade = lessonFacade;
    }

    public LessonFacade getLessonFacade() {
        return lessonFacade;
    }
}
