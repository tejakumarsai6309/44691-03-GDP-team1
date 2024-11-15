package com.GDP.TaskMasterDemo.Repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.GDP.TaskMasterDemo.Model.Task;
import com.GDP.TaskMasterDemo.Model.User;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByOwnerOrderByDateDesc(User user);
    
    @Query("SELECT t.owner.name, COUNT(t) FROM Task t GROUP BY t.owner.name")
    List<Object[]> countTasksByAssignee();
    
//    @Query("SELECT t.name FROM Task t where t.date > :startDate and t.date < :endDate")
//    List<String> findByDateBetween(@Param("startDate") LocalDateTime startDate,@Param("endDate") LocalDateTime endDate);

     List<Task> findByDateBetween(LocalDateTime startDate,LocalDateTime endDate);
     List<Task> findByNameContainingIgnoreCase(String name);

    
}