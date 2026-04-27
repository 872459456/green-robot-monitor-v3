package com.greenrobot.monitor.service;

import com.greenrobot.monitor.entity.IterationLog;
import com.greenrobot.monitor.repository.IterationLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class IterationLogService {

    private final IterationLogRepository repository;

    public List<IterationLog> getAllLogs() {
        return repository.findAllByOrderByIterationDateDesc();
    }

    public Optional<IterationLog> getLogById(Long id) {
        return repository.findById(id);
    }

    public List<IterationLog> getLogsByVersion(String version) {
        return repository.findByVersionOrderByIterationDateDesc(version);
    }

    public List<IterationLog> getLogsByPhase(String phase) {
        return repository.findByPhaseOrderByIterationDateDesc(phase);
    }

    public List<IterationLog> getLogsByStatus(String status) {
        return repository.findByStatusOrderByIterationDateDesc(status);
    }

    @Transactional
    public IterationLog createLog(IterationLog log) {
        return repository.save(log);
    }

    @Transactional
    public IterationLog updateLog(Long id, IterationLog updated) {
        Optional<IterationLog> existing = repository.findById(id);
        if (existing.isPresent()) {
            IterationLog log = existing.get();
            log.setVersion(updated.getVersion());
            log.setPhase(updated.getPhase());
            log.setIterationDate(updated.getIterationDate());
            log.setRequirement(updated.getRequirement());
            log.setChanges(updated.getChanges());
            log.setDecisionBasis(updated.getDecisionBasis());
            log.setOwner(updated.getOwner());
            log.setStatus(updated.getStatus());
            log.setNotes(updated.getNotes());
            return repository.save(log);
        }
        throw new RuntimeException("IterationLog not found with id: " + id);
    }

    @Transactional
    public void deleteLog(Long id) {
        repository.deleteById(id);
    }

    /** 获取当前活跃的迭代 */
    public Optional<IterationLog> getActiveIteration() {
        return repository.findByStatusOrderByIterationDateDesc("IN_PROGRESS")
                .stream().findFirst();
    }
}
