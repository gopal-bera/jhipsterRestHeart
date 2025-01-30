package com.mycompany.myapp.feigns;



import java.util.List;
import java.util.Optional;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.mycompany.myapp.domain.Student;

@FeignClient(name = "student-feign", url = "http://localhost:8080/jhipsterRestHeart")
public interface StudentFeign {

    @PostMapping("/student")
    public ResponseEntity<Void> save(@RequestBody Student student);

    @PutMapping("/student/{id}")
    public ResponseEntity<Void> update(@PathVariable String id, @RequestBody Student student);

    @GetMapping("/student")
    public ResponseEntity<List<Student>> getAll();

    @GetMapping("/student/{id}")
    public Optional<Student> getStudentById(@PathVariable("id") String id);

    @DeleteMapping("/student/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") String id);

}
