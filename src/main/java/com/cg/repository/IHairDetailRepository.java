package com.cg.repository;

import com.cg.domain.HairDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IHairDetailRepository extends JpaRepository<HairDetail,Long> {
    @Query(value = "SELECT b FROM HairDetail b " +
            "WHERE " +
            "LOWER(b.name) LIKE LOWER(:search) ")
    Page<HairDetail> searchEverything(String search, Pageable pageable);
}
