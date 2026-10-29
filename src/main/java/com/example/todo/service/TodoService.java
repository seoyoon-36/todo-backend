package com.example.todo.service;

import com.example.todo.dto.TodoRequest;
import com.example.todo.entity.Todo;
import com.example.todo.repository.TodoRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class TodoService {

    private final TodoRepository todoRepository;

    //생성자 주입
    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    // 전체 조회
    public List<Todo> findAll() {
        return todoRepository.findAll();
    }

    // 상세 조회
    public Todo findById(Long id) {
        return todoRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Todo not found"
                        ));
    }

    // 생성
    @Transactional
    public Todo create(TodoRequest request) {
        Todo todo = new Todo(request.title());
        return todoRepository.save(todo);
    }

    // 수정
    @Transactional
    public Todo update(Long id, TodoRequest request) {
        Todo todo = findById(id);

        todo.update(
                request.title(),
                request.completed()
        );

        return todo;
    }

    // 삭제
    @Transactional
    public void delete(Long id) {
        Todo todo = findById(id);
        todoRepository.delete(todo);
    }
}
