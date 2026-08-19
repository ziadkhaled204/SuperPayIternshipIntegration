package com.example.mockserver;

import com.fasterxml.jackson.databind.JsonNode;

public record MockApi(
        String id,
        String method,
        String path,
        int status,
        JsonNode response
) {
}