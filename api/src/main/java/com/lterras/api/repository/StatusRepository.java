package com.lterras.api.repository;

import com.lterras.api.model.EStatus;
import com.lterras.api.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusRepository extends JpaRepository<Status, Long> {

    Status findByName(EStatus name);

}
