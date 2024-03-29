package com.cg.service.stylistImageService;

import com.cg.domain.StylistImage;
import com.cg.repository.IStylistImageRepository;
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
public class StylistImageService  {

    private final Cloudinary cloudinary;

    private final IStylistImageRepository fileRepository;

    private final UploadUtils uploadUtils;


    public StylistImage saveAvatar(MultipartFile avatar)throws IOException {
        var file = new StylistImage();
        fileRepository.save(file);

        var uploadResult = cloudinary.uploader().upload(avatar.getBytes(), uploadUtils.buildImageStylistUploadParams(file));

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
