package com.cg.service.stylistService;

import com.cg.domain.Enum.EStatusStylist;
import com.cg.domain.Stylist;
import com.cg.domain.StylistImage;
import com.cg.repository.IStylistImageRepository;
import com.cg.repository.IStylistRepository;
import com.cg.service.dto.request.SelectOptionRequest;
import com.cg.service.dto.response.SelectOptionResponse;
import com.cg.service.stylistService.stylistRequest.StylistSaveRequest;
import com.cg.service.stylistService.stylistResponse.StylistDetailResponse;
import com.cg.service.stylistService.stylistResponse.StylistListResponse;
import com.cg.utils.AppUtils;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class StylistService {
    private final IStylistRepository stylistRepository;
    private final IStylistImageRepository fileRepository;

    public List<SelectOptionResponse> findAll(){
        return stylistRepository.findAllByStatusFree().stream()
                .map(stylist -> new SelectOptionResponse(stylist.getId().toString(), stylist.getName()))
                .collect(Collectors.toList());
    }

    public StylistDetailResponse findById(Long id){
        var stylist = stylistRepository.findById(id).orElse(new Stylist());
        var result = AppUtils.mapper.map(stylist, StylistDetailResponse.class);
        List<String> images = stylist.getStylistImage()
                .stream()
                .map(StylistImage::getFileUrl)
                .collect(Collectors.toList());
        result.setImages(images);
        return result;
    }

    public Page<StylistListResponse> getStylists(Pageable pageable, String search) {
        search = "%" + search + "%";
        return stylistRepository.searchEverything(search ,pageable).map(e -> {
            var result = AppUtils.mapper.map(e, StylistListResponse.class);
            result.setImages(
                    e.getStylistImage().stream()
//                            .map(StylistImage::getFileUrl)  // Lấy ra URL của mỗi ảnh
                            .map(StylistImage -> StylistImage.getFileUrl())
                            .collect(Collectors.toList())  // Tạo thành một danh sách
            );
            return result;
        });
    }

    public void create(StylistSaveRequest request) {
        var stylist = AppUtils.mapper.map(request, Stylist.class);

        stylist = stylistRepository.save(stylist);
        var files = fileRepository.findAllById(request.getFiles().stream().map(SelectOptionRequest::getId).collect(Collectors.toList()));
        for (var file: files) {
            file.setStylist(stylist);
        }
        fileRepository.saveAll(files);
    }

    public void update(StylistSaveRequest request, Long id) {
        var stylist = stylistRepository.findById(id).orElse(new Stylist());
        AppUtils.mapper.map(request, stylist);
        stylist = stylistRepository.save(stylist);

//        for(StylistImage image: stylist.getStylistImage()){
//            fileRepository.delete(image);
//        }
        var files = fileRepository.findAllById(request.getFiles().stream().map(SelectOptionRequest::getId).collect(Collectors.toList()));
        for (var file: files) {
            file.setStylist(stylist);
        }
        fileRepository.saveAll(files);
    }

    public Boolean delete(Long id) {
        Optional<Stylist> stylistOptional = stylistRepository.findById(id);

        if (stylistOptional.isPresent()) {
//            fileRepository.deleteHairDetailImageByHairDetailId(id);
            for(StylistImage image: stylistOptional.get().getStylistImage()){
                fileRepository.delete(image);
            }

            stylistRepository.deleteById(id);
            return true;
        } else {
            return false; // Không tìm thấy phòng để xóa
        }
    }

    public void changeStatus(Long id, String status) {
        var stylist = stylistRepository.findById(id).orElse(new Stylist());
        stylist.setStatus(EStatusStylist.valueOf(status));
        stylistRepository.save(stylist);
    }

    public List<StylistDetailResponse> getAll() {
        return stylistRepository.findAll().stream().map(stylist -> {
            var result = new StylistDetailResponse();
            result.setName(stylist.getName());
            List<String> images = stylist.getStylistImage().stream().map(StylistImage::getFileUrl)
                    .collect(Collectors.toList());
            result.setImages(images);
            return result;
        }).collect(Collectors.toList());
    }
}
