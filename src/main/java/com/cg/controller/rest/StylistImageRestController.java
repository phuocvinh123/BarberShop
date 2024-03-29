package com.cg.controller.rest;


import com.cg.domain.StylistImage;
import com.cg.service.stylistImageService.StylistImageService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/stylistImages")
@AllArgsConstructor
public class StylistImageRestController {
    private final StylistImageService uploadFileService;

    @PostMapping
    public StylistImage upload(@RequestParam("avatar") MultipartFile avatar) throws IOException {
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
