package com.example.AerionSports_BE.realtime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

@Service
public class CatalogRealtimeService {

    private static final long SSE_TIMEOUT = 0L;
    private final CopyOnWriteArraySet<SseEmitter> emitters = new CopyOnWriteArraySet<>();

    public SseEmitter subscribe() {
        SseEmitter emitter = new SseEmitter(SSE_TIMEOUT);
        emitters.add(emitter);

        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> {
            emitters.remove(emitter);
            emitter.complete();
        });
        emitter.onError(error -> emitters.remove(emitter));

        try {
            emitter.send(SseEmitter.event()
                    .name("connected")
                    .data(Map.of(
                            "type", "connected",
                            "timestamp", Instant.now().toString()
                    )));
        } catch (IOException ex) {
            emitters.remove(emitter);
            emitter.completeWithError(ex);
        }

        return emitter;
    }

    public void publishCatalogChange(String entityType, Integer entityId, String action) {
        publishAfterCommit(entityType, entityId, action);
    }

    private void publishAfterCommit(String entityType, Integer entityId, String action) {
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    broadcast(entityType, entityId, action);
                }
            });
            return;
        }

        broadcast(entityType, entityId, action);
    }

    private void broadcast(String entityType, Integer entityId, String action) {
        Map<String, Object> payload = Map.of(
                "type", "catalog-update",
                "entityType", entityType,
                "entityId", entityId,
                "action", action,
                "timestamp", Instant.now().toString()
        );

        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event()
                        .name("catalog-update")
                        .data(payload));
            } catch (IOException ex) {
                emitters.remove(emitter);
                emitter.completeWithError(ex);
            }
        }
    }
}
