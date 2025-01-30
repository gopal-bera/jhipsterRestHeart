package com.mycompany.myapp.feigns;


import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.mycompany.myapp.domain.Department;


@FeignClient(name = "department-client", url = "http://localhost:8080/jhipsterRestHeart")
public interface DepartmentFeign {

    @PostMapping("/department")
    public ResponseEntity<Void> save(@RequestBody Department department);

    @GetMapping("/department")
    public ResponseEntity<List<Department>> getAll();

    @GetMapping("/department/{id}")
    public ResponseEntity<Department> getDeptById(@PathVariable("id") String id);

    @PutMapping("/department/{id}")
    public ResponseEntity<Void> update(@PathVariable String id, @RequestBody Department department);

    @DeleteMapping("/department/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id);
}
