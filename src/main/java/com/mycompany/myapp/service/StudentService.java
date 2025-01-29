package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Student;
import com.mycompany.myapp.feigns.StudentFeign;
import com.mycompany.myapp.repository.StudentRepository;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link Student}.
 */
@Service
public class StudentService {

    private final Logger log = LoggerFactory.getLogger(StudentService.class);

    private final StudentRepository studentRepository;
    private final StudentFeign studentFeign;

    public StudentService(StudentRepository studentRepository, StudentFeign studentFeign) {
        this.studentRepository = studentRepository;
        this.studentFeign = studentFeign;
    }

    
    public ResponseEntity<Void> save(Student student) {
        log.debug("Request to save Student : {}", student);
      
        student.setId(new ObjectId().toHexString());
        return studentFeign.save(student);
    }

    
    public ResponseEntity<Void> update(String id, Student student) {
        log.debug("Request to save Student : {}", student);
        return studentFeign.update(id, student);
    }

    
    public Optional<Student> partialUpdate(Student student) {
        log.debug("Request to partially update Student : {}", student);

        return studentRepository
            .findById(student.getId())
            .map(existingStudent -> {
                if (student.getName() != null) {
                    existingStudent.setName(student.getName());
                }
                if (student.getEmail() != null) {
                    existingStudent.setEmail(student.getEmail());
                }
                if (student.getPhoneNo() != null) {
                    existingStudent.setPhoneNo(student.getPhoneNo());
                }
                if (student.getPercentage() != null) {
                    existingStudent.setPercentage(student.getPercentage());
                }
                if (student.getCourse() != null) {
                    existingStudent.setCourse(student.getCourse());
                }

                return existingStudent;
            })
            .map(studentRepository::save);
    }

    public ResponseEntity<List<Student>> findAll() {
        log.debug("Request to get all Students");
        return studentFeign.getAll();
        // return studentRepository.findAll();
    }

    
    public Optional<Student> findOne(String id) {
        log.debug("Request to get Student : {}", id);
        // return studentRepository.findById(id);
        return studentFeign.getStudentById(id);
    }

    /**
     * Delete the student by id.
     *
     * @param id the id of the entity.
     */
    public void delete(String id) {
        log.debug("Request to delete Student : {}", id);
        studentRepository.deleteById(id);
    }
}
