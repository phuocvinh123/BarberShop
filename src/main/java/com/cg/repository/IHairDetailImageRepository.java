package com.cg.repository;

import com.cg.domain.HairDetailImage;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IHairDetailImageRepository extends JpaRepository<HairDetailImage,String> {
    @Transactional
    void deleteHairDetailImageByHairDetailId(Long id);

    @Transactional
    void deleteImageByFileUrl(String fileUrl);
}
