package com.cg.utils;

import com.cg.domain.HairDetailImage;
import com.cg.domain.StylistImage;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class UploadUtils {
    public static final String IMAGE_UPLOAD_FOLDER = "barber";

    public Map buildImageUploadParams(HairDetailImage file) {
        if (file == null || file.getId() == null)
            throw new RuntimeException("Không thể upload hình ảnh của sản phẩm chưa được lưu");

        String publicId = String.format("%s/%s", IMAGE_UPLOAD_FOLDER, file.getId());

        return ObjectUtils.asMap(
                "public_id", publicId,
                "overwrite", true,
                "resource_type", "image"
        );
    }

    public Map buildImageStylistUploadParams(StylistImage file) {
        if (file == null || file.getId() == null)
            throw new RuntimeException("Không thể upload hình ảnh của sản phẩm chưa được lưu");

        String publicId = String.format("%s/%s", IMAGE_UPLOAD_FOLDER, file.getId());

        return ObjectUtils.asMap(
                "public_id", publicId,
                "overwrite", true,
                "resource_type", "image"
        );
    }

}