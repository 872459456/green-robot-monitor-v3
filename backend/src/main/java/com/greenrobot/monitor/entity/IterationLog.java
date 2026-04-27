package com.greenrobot.monitor.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 迭代日志实体
 * 
 * 记录每次迭代的需求、修改内容、决策依据
 */
@Data
@Entity
@Table(name = "iteration_logs")
public class IterationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 版本号，如 v3.0.0 */
    @Column(nullable = false, length = 20)
    private String version;

    /** 迭代阶段，如 "基础设施"、"架构升级" */
    @Column(nullable = false, length = 50)
    private String phase;

    /** 迭代日期 */
    @Column(nullable = false)
    private LocalDateTime iterationDate;

    /** 需求描述 */
    @Column(columnDefinition = "TEXT")
    private String requirement;

    /** 修改内容 (JSON数组格式) */
    @Column(columnDefinition = "TEXT")
    private String changes;

    /** 决策依据 */
    @Column(columnDefinition = "TEXT")
    private String decisionBasis;

    /** 负责人 */
    @Column(length = 50)
    private String owner;

    /** 状态: PLANNING / IN_PROGRESS / COMPLETED */
    @Column(nullable = false, length = 20)
    private String status;

    /** 备注 */
    @Column(columnDefinition = "TEXT")
    private String notes;

    /** 创建时间 */
    @Column(nullable = false)
    private LocalDateTime createdAt;

    /** 更新时间 */
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
