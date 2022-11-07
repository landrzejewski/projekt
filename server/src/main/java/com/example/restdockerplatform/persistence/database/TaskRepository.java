package com.example.restdockerplatform.persistence.database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, String> {
    List<Task> findByUsernameAndProject(String username, String project);

    List<Task> findByNodeUUId(String nodeUUId);

}
