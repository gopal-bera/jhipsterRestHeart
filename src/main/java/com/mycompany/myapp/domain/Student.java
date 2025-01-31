package com.mycompany.myapp.domain;

import java.io.Serializable;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mycompany.myapp.json.ObjectIdToStringDeserializer;
import com.mycompany.myapp.json.StringToObjectIdSerializer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * A Student.
 */
@Document(collection = "student")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Student implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = StringToObjectIdSerializer.class)
    @JsonDeserialize(using = ObjectIdToStringDeserializer.class )
    @JsonProperty("_id")
    @Field("_id")
    private String id;

    @Field("name")
    private String name;

    @Field("email")
    private String email;

    @Field("phone_no")
    private Long phoneNo;

    @Field("percentage")
    private Double percentage;

    @Field("course")
    private String course;

    @Field("department")
    private RefType department;

    @Field("create_info")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private CreateInfo createInfo;

    @Field("update_info")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private UpdateInfo updateInfo;

    
    public Student id(String id) {
        this.setId(id);
        return this;
    }


    public Student name(String name) {
        this.setName(name);
        return this;
    }


    public Student email(String email) {
        this.setEmail(email);
        return this;
    }


    public Student phoneNo(Long phoneNo) {
        this.setPhoneNo(phoneNo);
        return this;
    }

    
    public Student percentage(Double percentage) {
        this.setPercentage(percentage);
        return this;
    }


    public Student course(String course) {
        this.setCourse(course);
        return this;
    }
}
