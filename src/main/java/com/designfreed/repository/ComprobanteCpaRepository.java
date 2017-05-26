package com.designfreed.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@NoRepositoryBean
public interface ComprobanteCpaRepository<T, ID extends Serializable> extends CrudRepository<T, ID> {
    List<T> findTop100ByCodProveeAndFechaAfter(String codProvee, Date fecha);
}
