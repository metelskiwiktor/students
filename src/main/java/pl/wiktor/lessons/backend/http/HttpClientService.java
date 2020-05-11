package pl.wiktor.lessons.backend.http;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

abstract class HttpClientService <T> {
    private RestTemplate restTemplate = new RestTemplate();
    private String uri;
    private Class<T> tClass;

    HttpClientService(String uri, Class<T> tClass) {
        this.uri = uri;
        this.tClass = tClass;
    }

    public List<T> getAll(){
        return toObject(restTemplate.exchange(
                uri + "/all", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<T>>(){}).getBody());
    }

    public List<T> delete(T object){
        return toObject(restTemplate.exchange(
                uri + "/delete", HttpMethod.DELETE, new HttpEntity<>(object),
                new ParameterizedTypeReference<List<T>>(){}).getBody());
    }

    public List<T> update(T object){
        return toObject(restTemplate.exchange(
                uri + "/update", HttpMethod.PUT, new HttpEntity<>(object),
                new ParameterizedTypeReference<List<T>>(){}).getBody());
    }

    public List<T> add(T object){
        return toObject(restTemplate.exchange(
                uri + "/add", HttpMethod.POST, new HttpEntity<>(object),
                new ParameterizedTypeReference<List<T>>(){}).getBody());
    }

    private List<T> toObject(List<T> list){
        ObjectMapper mapper = new ObjectMapper()
                .registerModule(new ParameterNamesModule())
                .registerModule(new Jdk8Module())
                .registerModule(new JavaTimeModule())
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        CollectionType listType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, tClass);
        return mapper.convertValue(list, listType);
    }
}
