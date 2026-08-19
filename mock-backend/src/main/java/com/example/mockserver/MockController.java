package com.example.mockserver;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class MockController {

    private final MockRegistry registry;

    public MockController(MockRegistry registry) {
        this.registry = registry;
    }

    @PostMapping("/mock/register")
    public ResponseEntity<MockApi> register(@RequestBody MockApi api) {
        if (invalid(api)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(registry.register(normalize(api)));
    }

    @GetMapping("/mock/list")
    public List<MockApi> list() {
        return registry.all();
    }

    @PutMapping("/mock/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody MockApi api) {
        if (invalid(api)) {
            return ResponseEntity.badRequest().build();
        }
        return registry.update(id, normalize(api))
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "mock not found: " + id)));
    }

    @DeleteMapping("/mock/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        if (registry.delete(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "mock not found: " + id));
    }

    @RequestMapping("/**")
    public ResponseEntity<?> handle(HttpServletRequest request) {
        String uri = request.getRequestURI();
        if (uri.startsWith("/mock/")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "unknown mock endpoint: " + uri));
        }
        return registry.find(request.getMethod(), uri)
                .<ResponseEntity<?>>map(m -> ResponseEntity.status(m.status()).body(m.response()))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "no mock registered for "
                                + request.getMethod() + " " + uri)));
    }

    private boolean invalid(MockApi api) {
        return api.method() == null || api.method().isBlank() || api.path() == null || api.path().isBlank();
    }

    private MockApi normalize(MockApi api) {
        return new MockApi(
                null,
                api.method().trim().toUpperCase(),
                api.path().trim(),
                api.status() == 0 ? 200 : api.status(),
                api.response());
    }
}