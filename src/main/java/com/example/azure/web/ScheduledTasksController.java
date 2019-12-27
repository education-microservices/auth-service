package com.example.azure.web;

import com.example.azure.persistence.models.ScheduledTaskModel;
import com.example.azure.persistence.repository.ScheduledTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/tasks")
public class ScheduledTasksController {

    @Autowired
    private ScheduledTaskRepository scheduledTaskRepository;

    @PostMapping
    public ScheduledTaskModel createScheduledTask(@RequestBody  ScheduledTaskModel scheduledTaskModel) {

        scheduledTaskRepository.save(scheduledTaskModel);

        return scheduledTaskModel;
    }

    @GetMapping
    public List<ScheduledTaskModel> get() {
        return scheduledTaskRepository.findAll();
    }
}
