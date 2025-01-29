package com.mycompany.myapp.feigns;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.mycompany.myapp.domain.Department;

@FeignClient(name = "department-client", url = "http://localhost:8080/jhipsterRestHeart")
public interface DepartmentFeign {

    @PostMapping("/department")
    // public Department save(@RequestBody Department department);
    public ResponseEntity<Void> save(@RequestBody Department department);

}
