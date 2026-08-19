package com.example.mockserver;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class MockRegistry {

    private final List<MockApi> apis = new ArrayList<>();

    public synchronized MockApi register(MockApi api) {
        MockApi saved = new MockApi(
                UUID.randomUUID().toString(),
                api.method(),
                api.path(),
                api.status(),
                api.response());
        apis.add(saved);
        return saved;
    }

    public synchronized Optional<MockApi> find(String id) {
        return apis.stream().filter(a -> id.equals(a.id())).findFirst();
    }

    public synchronized Optional<MockApi> find(String requestMethod, String requestPath) {
        return apis.stream()
                .filter(a -> a.method().equalsIgnoreCase(requestMethod))
                .filter(a -> a.path().equals(requestPath))
                .findFirst();
    }

    public synchronized List<MockApi> all() {
        return new ArrayList<>(apis);
    }

    public synchronized Optional<MockApi> update(String id, MockApi api) {
        for (int i = 0; i < apis.size(); i++) {
            if (id.equals(apis.get(i).id())) {
                MockApi updated = new MockApi(id, api.method(), api.path(), api.status(), api.response());
                apis.set(i, updated);
                return Optional.of(updated);
            }
        }
        return Optional.empty();
    }

    public synchronized boolean delete(String id) {
        for (int i = 0; i < apis.size(); i++) {
            if (id.equals(apis.get(i).id())) {
                apis.remove(i);
                return true;
            }
        }
        return false;
    }
}