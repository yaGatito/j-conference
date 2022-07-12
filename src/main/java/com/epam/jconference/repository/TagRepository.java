package com.epam.jconference.repository;

import com.epam.jconference.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    @Override
    List<Tag> findAllById(Iterable<Long> longs);
}
