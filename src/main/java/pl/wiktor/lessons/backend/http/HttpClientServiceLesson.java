package pl.wiktor.lessons.backend.http;

import pl.wiktor.lessons.backend.model.Lesson;

public class HttpClientServiceLesson extends HttpClientService<Lesson> {
    private static final String LESSONS_ADDRESS = "http://localhost:8085/lessons";

    public HttpClientServiceLesson() {
        super(LESSONS_ADDRESS, Lesson.class);
    }
}
