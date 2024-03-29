package com.cg.controller.rest;

import com.cg.domain.HairDetailImage;
import com.cg.service.hairDetailImageService.HairDetailImageService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
@RestController
@RequestMapping("/api/hairDetailImages")
@AllArgsConstructor
public class HairDetailImageRestController {
    private final HairDetailImageService uploadFileService;

    @PostMapping
    public HairDetailImage upload(@RequestParam("avatar") MultipartFile avatar) throws IOException {
        return uploadFileService.saveAvatar(avatar);
    }

    @DeleteMapping()
    public void delete(@RequestParam("url") String url) {
        uploadFileService.delete(url);
    }

    @DeleteMapping("{id}")
    public void deleteById(@PathVariable String id) {
        uploadFileService.deleteById(id);
    }
}
