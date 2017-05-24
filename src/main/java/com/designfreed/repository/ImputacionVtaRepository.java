package com.designfreed.repository;

import com.designfreed.entities.ImputacionVta;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImputacionVtaRepository extends CrudRepository<ImputacionVta, Long> {
    List<ImputacionVta> findByNCompCanAndTCompCan(String nCompCan, String tCompCan);
}
