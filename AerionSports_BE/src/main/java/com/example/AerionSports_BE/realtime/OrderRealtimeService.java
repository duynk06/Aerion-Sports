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
public class OrderRealtimeService {

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

    public void publishOrderChange(Integer hoaDonId, String maHoaDon, Integer statusIndex, String statusName, String action) {
        publishAfterCommit(hoaDonId, maHoaDon, statusIndex, statusName, action);
    }

    private void publishAfterCommit(Integer hoaDonId, String maHoaDon, Integer statusIndex, String statusName, String action) {
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    broadcast(hoaDonId, maHoaDon, statusIndex, statusName, action);
                }
            });
            return;
        }

        broadcast(hoaDonId, maHoaDon, statusIndex, statusName, action);
    }

    private void broadcast(Integer hoaDonId, String maHoaDon, Integer statusIndex, String statusName, String action) {
        Map<String, Object> payload = Map.of(
                "type", "order-update",
                "hoaDonId", hoaDonId,
                "maHoaDon", maHoaDon,
                "statusIndex", statusIndex,
                "statusName", statusName,
                "action", action,
                "timestamp", Instant.now().toString()
        );

        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event()
                        .name("order-update")
                        .data(payload));
            } catch (IOException ex) {
                emitters.remove(emitter);
                emitter.completeWithError(ex);
            }
        }
    }
}
