package com.greenrobot.monitor.repository;

import com.greenrobot.monitor.entity.IterationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface IterationLogRepository extends JpaRepository<IterationLog, Long> {
    
    /** 按版本号查找 */
    List<IterationLog> findByVersionOrderByIterationDateDesc(String version);
    
    /** 按阶段查找 */
    List<IterationLog> findByPhaseOrderByIterationDateDesc(String phase);
    
    /** 按状态查找 */
    List<IterationLog> findByStatusOrderByIterationDateDesc(String status);
    
    /** 查找所有按日期倒序 */
    List<IterationLog> findAllByOrderByIterationDateDesc();
}
