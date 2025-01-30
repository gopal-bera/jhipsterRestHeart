package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Department;
import com.mycompany.myapp.feigns.DepartmentFeign;
import com.mycompany.myapp.repository.DepartmentRepository;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link Department}.
 */
@Service
public class DepartmentService {

    private final Logger log = LoggerFactory.getLogger(DepartmentService.class);

    private final DepartmentRepository departmentRepository;
    private final DepartmentFeign departmentFeign;

    public DepartmentService(DepartmentRepository departmentRepository, DepartmentFeign departmentFeign) {
        this.departmentRepository = departmentRepository;
        this.departmentFeign = departmentFeign;
    }

    public ResponseEntity<Void> save(Department department) {
        log.debug("Request to save Department : {}", department);
        department.setId(new ObjectId().toHexString());
        return departmentFeign.save(department);
        // return departmentRepository.save(department);
    }

    
    public ResponseEntity<Void> update(String id, Department department) {
        log.debug("Request to save Department : {}", department);
        return departmentFeign.update(id, department);
        // return departmentRepository.save(department);
    }

    
    public Optional<Department> partialUpdate(Department department) {
        log.debug("Request to partially update Department : {}", department);

        return departmentRepository
            .findById(department.getId())
            .map(existingDepartment -> {
                if (department.getName() != null) {
                    existingDepartment.setName(department.getName());
                }
                if (department.getUniversity() != null) {
                    existingDepartment.setUniversity(department.getUniversity());
                }

                return existingDepartment;
            })
            .map(departmentRepository::save);
    }


    public ResponseEntity<List<Department>> findAll() {
        log.debug("Request to get all Departments");
        return departmentFeign.getAll();
        // return departmentRepository.findAll();
    }

    
    public Optional<Department> findOne(String id) {
        log.debug("Request to get Department : {}", id);
        return departmentFeign.getDeptById(id);
        // return departmentRepository.findById(id);
    }

  
    public void delete(String id) {
        log.debug("Request to delete Department : {}", id);
        // departmentRepository.deleteById(id);
        departmentFeign.delete(id);
    }
}
