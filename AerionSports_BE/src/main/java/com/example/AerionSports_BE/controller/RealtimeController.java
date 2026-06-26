package com.example.AerionSports_BE.controller;

import com.example.AerionSports_BE.realtime.CatalogRealtimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/realtime")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class RealtimeController {

    private final CatalogRealtimeService catalogRealtimeService;

    @GetMapping(value = "/catalog/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamCatalogUpdates() {
        return catalogRealtimeService.subscribe();
    }
}
