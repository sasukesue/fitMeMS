package com.proj.fitnessclass.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.util.Objects;

import com.proj.fitnessclass.domain.enumeration.Level;

import com.proj.fitnessclass.domain.enumeration.Type;

/**
 * A FitnessClass.
 */
@Entity
@Table(name = "fitness_class")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "fitnessclass")
public class FitnessClass implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "class_name", nullable = false)
    private String className;

    @NotNull
    @Column(name = "duration", nullable = false)
    private Integer duration;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "level", nullable = false)
    private Level level;

    @NotNull
    @Column(name = "instructor_name", nullable = false)
    private String instructorName;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private Type type;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public FitnessClass className(String className) {
        this.className = className;
        return this;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Integer getDuration() {
        return duration;
    }

    public FitnessClass duration(Integer duration) {
        this.duration = duration;
        return this;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Level getLevel() {
        return level;
    }

    public FitnessClass level(Level level) {
        this.level = level;
        return this;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public String getInstructorName() {
        return instructorName;
    }

    public FitnessClass instructorName(String instructorName) {
        this.instructorName = instructorName;
        return this;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }

    public Type getType() {
        return type;
    }

    public FitnessClass type(Type type) {
        this.type = type;
        return this;
    }

    public void setType(Type type) {
        this.type = type;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FitnessClass)) {
            return false;
        }
        return id != null && id.equals(((FitnessClass) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "FitnessClass{" +
            "id=" + getId() +
            ", className='" + getClassName() + "'" +
            ", duration=" + getDuration() +
            ", level='" + getLevel() + "'" +
            ", instructorName='" + getInstructorName() + "'" +
            ", type='" + getType() + "'" +
            "}";
    }
}
