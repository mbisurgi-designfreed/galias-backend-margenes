package com.designfreed.repository;

import com.designfreed.entities.ImputacionCpa;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImputacionCpaRepository extends CrudRepository<ImputacionCpa, Long> {
    List<ImputacionCpa> findByNCompCanAndTCompCan(String nCompCan, String tCompCan);
}
