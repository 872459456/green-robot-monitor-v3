package com.greenrobot.monitor.controller;

import com.greenrobot.monitor.entity.IterationLog;
import com.greenrobot.monitor.service.IterationLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/iterations")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class IterationLogController {

    private final IterationLogService service;

    @GetMapping
    public ResponseEntity<List<IterationLog>> getAllLogs() {
        return ResponseEntity.ok(service.getAllLogs());
    }

    @GetMapping("/{id}")
    public ResponseEntity<IterationLog> getLogById(@PathVariable Long id) {
        return service.getLogById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/version/{version}")
    public ResponseEntity<List<IterationLog>> getLogsByVersion(@PathVariable String version) {
        return ResponseEntity.ok(service.getLogsByVersion(version));
    }

    @GetMapping("/phase/{phase}")
    public ResponseEntity<List<IterationLog>> getLogsByPhase(@PathVariable String phase) {
        return ResponseEntity.ok(service.getLogsByPhase(phase));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<IterationLog>> getLogsByStatus(@PathVariable String status) {
        return ResponseEntity.ok(service.getLogsByStatus(status));
    }

    @GetMapping("/active")
    public ResponseEntity<IterationLog> getActiveIteration() {
        return service.getActiveIteration()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<IterationLog> createLog(@RequestBody IterationLog log) {
        return ResponseEntity.ok(service.createLog(log));
    }

    @PutMapping("/{id}")
    public ResponseEntity<IterationLog> updateLog(@PathVariable Long id, @RequestBody IterationLog log) {
        return ResponseEntity.ok(service.updateLog(id, log));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLog(@PathVariable Long id) {
        service.deleteLog(id);
        return ResponseEntity.noContent().build();
    }
}
