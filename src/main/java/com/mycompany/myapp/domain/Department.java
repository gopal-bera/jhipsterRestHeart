package com.mycompany.myapp.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mycompany.myapp.json.ObjectIdToStringDeserializer;
import com.mycompany.myapp.json.StringToObjectIdSerializer;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;



@Document(collection = "department")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@JsonInclude(Include.NON_NULL)
public class Department implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = StringToObjectIdSerializer.class)
    @JsonDeserialize(using = ObjectIdToStringDeserializer.class )
    @JsonProperty("_id")
    @Field("_id")
    private String id;

    @Field("name")
    private String name;

    @Field("university")
    private String university;

    @Field("student")
    List<RefType> student = new ArrayList<>();

    @Field("create_info")
    private CreateInfo createInfo;

    @Field("update_info")
    private UpdateInfo updateInfo;

    public Department id(String id) {
        this.setId(id);
        return this;
    }

    
    public Department name(String name) {
        this.setName(name);
        return this;
    }

    
    public Department university(String university) {
        this.setUniversity(university);
        return this;
    }

    
    public Department addStudents(RefType refType) {
        this.student.add(refType);
        return this;
    }
}
