package com.cg.repository;

import com.cg.domain.StylistImage;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IStylistImageRepository extends JpaRepository<StylistImage,String> {
    @Transactional
    void deleteImageByFileUrl(String fileUrl);
}
