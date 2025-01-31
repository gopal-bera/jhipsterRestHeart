package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.CreateInfo;
import com.mycompany.myapp.domain.Department;
import com.mycompany.myapp.domain.RefType;
import com.mycompany.myapp.domain.Student;
import com.mycompany.myapp.domain.UpdateInfo;
import com.mycompany.myapp.domain.RefType.RefTo;
import com.mycompany.myapp.feigns.DepartmentFeign;
import com.mycompany.myapp.feigns.StudentFeign;
import com.mycompany.myapp.repository.StudentRepository;
import com.mycompany.myapp.repository.UserRepository;
import com.mycompany.myapp.service.StudentService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;


@RestController
@RequestMapping("/api")
public class StudentResource {

    private final Logger log = LoggerFactory.getLogger(StudentResource.class);

    private static final String ENTITY_NAME = "student";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StudentService studentService;

    private final StudentRepository studentRepository;
    private final StudentFeign studentFeign;
    private final DepartmentFeign departmentFeign;
    private final UserRepository userRepository;


    public StudentResource(StudentService studentService, StudentRepository studentRepository, StudentFeign studentFeign, DepartmentFeign departmentFeign, UserRepository userRepository) {
        this.studentService = studentService;
        this.studentRepository = studentRepository;
        this.studentFeign = studentFeign;
        this.departmentFeign = departmentFeign;
        this.userRepository = userRepository;
    }

    @PostMapping("/students")
    public ResponseEntity<?> createStudent(@RequestBody Student student) throws URISyntaxException {
        log.debug("REST request to save Student : {}", student);
        if (student.getId() != null) {
            throw new BadRequestAlertException("A new student cannot already have an ID", ENTITY_NAME, "idexists");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        student.setCreateInfo(CreateInfo.builder()
                .user(new RefType(userRepository.findOneByLogin(currentPrincipalName).get().getId(), RefTo.User))
                .createdDate(Instant.now())
                .build());

        student.setUpdateInfo(UpdateInfo.builder()
                .user(new RefType(userRepository.findOneByLogin(currentPrincipalName).get().getId(), RefTo.User))
                .lastModifiedDate(Instant.now())
                .build());


        ResponseEntity<Void> save = studentService.save(student);
        return save;
    }

    
    @PutMapping("/students/{id}")
    public ResponseEntity<?> updateStudent(
        @PathVariable(value = "id", required = false) String id,
        @RequestBody Student student
    ) throws URISyntaxException {
        log.debug("REST request to update Student : {}, {}", id, student);
        
        // if id n/ot given then it will take as new doc
        if (student.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!id.equals(student.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        ResponseEntity<Void> update = studentService.update(id, student);
        return update;
    }

  
    @PatchMapping(value = "/students/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Student> partialUpdateStudent(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody Student student
    ) throws URISyntaxException {
        log.debug("REST request to partial update Student partially : {}, {}", id, student);
        if (student.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, student.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!studentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Student> result = studentService.partialUpdate(student);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, student.getId())
        );
    }

    @GetMapping("/students")
    public ResponseEntity<List<Student>> getAllStudents() {
        log.debug("REST request to get all Students");
        return studentService.findAll();
    }

    @GetMapping("/students/{id}")
    public ResponseEntity<Student> getStudent(@PathVariable String id) {
        log.debug("REST request to get Student : {}", id);
        Optional<Student> student = studentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(student);
    }

    @DeleteMapping("/students/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable String id) {
        log.debug("REST request to delete Student : {}", id);
        studentService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }


    @PostMapping("/students/{deptId}/student")
    public ResponseEntity<?> addStudentToDepartment(@PathVariable String deptId, @RequestBody Student student)throws URISyntaxException {
        
        log.debug("request to add student {} to departmentId {}", student, deptId);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        student.setCreateInfo(CreateInfo.builder()
                .user(new RefType(userRepository.findOneByLogin(currentPrincipalName).get().getId(), RefTo.User))
                .createdDate(Instant.now())
                .build());

        student.setUpdateInfo(UpdateInfo.builder()
                .user(new RefType(userRepository.findOneByLogin(currentPrincipalName).get().getId(), RefTo.User))
                .lastModifiedDate(Instant.now())
                .build());

        student.setId(new ObjectId().toHexString());
        student.setDepartment(new RefType( new ObjectId(deptId).toHexString(), RefTo.Department));

        ResponseEntity<Void> addStudentToDepartment = studentFeign.save(student);
        String location = addStudentToDepartment.getHeaders().get("Location").get(0);
        String studentId = location.substring(location.lastIndexOf("/")+1);

        ResponseEntity<Department> byId = departmentFeign.getDeptById(deptId);
        Department department = byId.getBody();
        department.getStudent().add(new RefType( new ObjectId(studentId).toHexString(), RefTo.Student));
        departmentFeign.update(deptId, department);
        return addStudentToDepartment;
    }
}