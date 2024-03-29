package com.cg.service.hairDetailImageService;

import com.cg.domain.HairDetailImage;
import com.cg.repository.IHairDetailImageRepository;
import com.cg.utils.UploadUtils;
import com.cloudinary.Cloudinary;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@AllArgsConstructor
@Service
@Transactional
public class HairDetailImageService  {

    private final Cloudinary cloudinary;

    private final IHairDetailImageRepository fileRepository;

    private final UploadUtils uploadUtils;

    public HairDetailImage saveAvatar(MultipartFile avatar) throws IOException {
        var file = new HairDetailImage();
        fileRepository.save(file);

        var uploadResult = cloudinary.uploader().upload(avatar.getBytes(), uploadUtils.buildImageUploadParams(file));

        String fileUrl = (String) uploadResult.get("secure_url");
        String fileFormat = (String) uploadResult.get("format");

        file.setFileName(file.getId() + "." + fileFormat);
        file.setFileUrl(fileUrl);
        file.setFileFolder(UploadUtils.IMAGE_UPLOAD_FOLDER);
        file.setCloudId(file.getFileFolder() + "/" + file.getId());
        fileRepository.save(file);
        return file;
    }

    public void delete(String fileUrl) {
        fileRepository.deleteImageByFileUrl(fileUrl);
    }

    public void deleteById(String id) {
        fileRepository.deleteById(id);
    }
}
