package com.epam.jconference.model;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Map;
import java.util.Objects;

@Getter
@Setter
@ToString
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    @Transient
    private Map<String, String> translations;

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        Tag tag = (Tag) o;
        return id != null && Objects.equals(id, tag.id);
    }
}
