package com.example.todo.controller;

import com.example.todo.dto.TodoRequest;
import com.example.todo.entity.Todo;
import com.example.todo.service.TodoService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todos")
@CrossOrigin(origins = "*")
public class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    // 전체 조회
    @GetMapping
    public List<Todo> getAll() {
        return todoService.findAll();
    }

    // 상세 조회
    @GetMapping("/{id}")
    public Todo getOne(@PathVariable Long id) {
        return todoService.findById(id);
    }

    // 생성
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Todo create(@Valid @RequestBody TodoRequest request) {
        return todoService.create(request);
    }

    // 수정
    @PatchMapping("/{id}")
    public Todo update(
            @PathVariable Long id,
            @Valid @RequestBody TodoRequest request
    ) {
        return todoService.update(id, request);
    }

    // 삭제
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        todoService.delete(id);
    }
}
