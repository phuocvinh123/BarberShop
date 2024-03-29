package com.cg.repository;

import com.cg.domain.HairDetail;
import com.cg.domain.Stylist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface IStylistRepository extends JpaRepository<Stylist,Long> {
    @Query(value = "SELECT st from Stylist st WHERE st.status = 'FREE' ")
    List<Stylist> findAllByStatusFree();

    @Query(value = "SELECT s FROM Stylist s " +
            "WHERE " +
            "lower(s.name) LIKE lower(:search)")
    Page<Stylist> searchEverything(String search, Pageable pageable);
}
