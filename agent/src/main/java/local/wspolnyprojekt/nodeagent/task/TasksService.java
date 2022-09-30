package local.wspolnyprojekt.nodeagent.task;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.synchronizedMap;

@Service
public class TasksService {
    Map<String, Task> tasks = synchronizedMap(new HashMap<>());

    public boolean hasTash(String taskId) {
        return tasks.containsKey(taskId);
    }

    public Task getTask(String taskId) {
        return tasks.get(taskId);
    }

    public void addTask(Task task) {
        tasks.put(task.getTaskId(),task);
    }

    public void deleteTask(String taskId) {
        tasks.remove(taskId);
    }

}
