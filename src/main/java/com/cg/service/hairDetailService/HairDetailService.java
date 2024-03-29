package com.cg.service.hairDetailService;

import com.cg.domain.HairDetail;
import com.cg.domain.HairDetailImage;
import com.cg.repository.IHairDetailImageRepository;
import com.cg.repository.IHairDetailRepository;
import com.cg.service.dto.request.SelectOptionRequest;
import com.cg.service.dto.response.SelectOptionServiceResponse;
import com.cg.service.hairDetailService.hairDetailRequest.HairDetailSaveRequest;
import com.cg.service.hairDetailService.hairDetailResponse.HairDetailListResponse;
import com.cg.service.hairDetailService.hairDetailResponse.HairDetailResponse;
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
public class HairDetailService {
    private final IHairDetailRepository hairDetailRepository;
    private final IHairDetailImageRepository fileRepository;
//    private final IHairDetailImageRepository hairDetailImageRepository;



    public List<SelectOptionServiceResponse> findAll(){
        return hairDetailRepository.findAll().stream()
                .map(hairDetail -> new SelectOptionServiceResponse(hairDetail.getId().toString(), hairDetail.getName(),hairDetail.getPrice().toString()))
                .collect(Collectors.toList());
    }

    public HairDetailResponse findById(Long id){
        var hairDetail = hairDetailRepository.findById(id).orElse(new HairDetail());
        var result = AppUtils.mapper.map(hairDetail, HairDetailResponse.class);
        List<String> images = hairDetail.getHairDetailImages()
                .stream()
                .map(HairDetailImage::getFileUrl)
                .collect(Collectors.toList());
        result.setImages(images);
        return result;
    }

//    public Page<HairDetailListResponse> getHairDetails(Pageable pageable){
//        return hairDetailRepository.findAll(pageable).map(e -> {
//            var result = AppUtil.mapper.map(e, HairDetailListResponse.class);
//            result.setImages(
//                    e.getHairDetailImages().stream()
//                            .map(HairDetailImage::getFileUrl)  // Lấy ra URL của mỗi ảnh
//                            .collect(Collectors.toList())  // Tạo thành một danh sách
//            );
//            return result;
//        });
//    }

    public Page<HairDetailListResponse> getHairDetails(Pageable pageable, String search){
        search = "%" + search + "%";
        return hairDetailRepository.searchEverything(search ,pageable).map(e -> {
            var result = AppUtils.mapper.map(e, HairDetailListResponse.class);
            result.setImages(
                    e.getHairDetailImages().stream()
                            .map(HairDetailImage::getFileUrl)  // Lấy ra URL của mỗi ảnh
                            .collect(Collectors.toList())  // Tạo thành một danh sách
            );
            return result;
        });
    }

    public void create(HairDetailSaveRequest request){
        var hairDetail = AppUtils.mapper.map(request, HairDetail.class);

        hairDetail = hairDetailRepository.save(hairDetail);
        var files = fileRepository.findAllById(request.getFiles().stream().map(SelectOptionRequest::getId).collect(Collectors.toList()));
        for (var file: files) {
            file.setHairDetail(hairDetail);
        }
        fileRepository.saveAll(files);
    }


    public void update(HairDetailSaveRequest request, Long id) {
        var hairDetail = hairDetailRepository.findById(id).orElse(new HairDetail());
//        var hairDetail = AppUtil.mapper.map(request, HairDetail.class);
        AppUtils.mapper.map(request, hairDetail);
        hairDetail = hairDetailRepository.save(hairDetail);

//        for(HairDetailImage image: hairDetail.getHairDetailImages()){
//            fileRepository.delete(image);
//        }
        var files = fileRepository.findAllById(request.getFiles().stream().map(SelectOptionRequest::getId).collect(Collectors.toList()));
        for (var file: files) {
            file.setHairDetail(hairDetail);
        }
        fileRepository.saveAll(files);
    }

    public Boolean delete(Long id) {
        Optional<HairDetail> hairDetailOptional = hairDetailRepository.findById(id);

        if (hairDetailOptional.isPresent()) {
            fileRepository.deleteHairDetailImageByHairDetailId(id);
            hairDetailRepository.deleteById(id);
            return true;
        } else {
            return false; // Không tìm thấy phòng để xóa
        }
    }

    public List<HairDetailResponse> getAll() {
        return hairDetailRepository.findAll().stream().map(hairDetail -> {
            var result = new HairDetailResponse();
            result.setName(hairDetail.getName());
            result.setDescription(hairDetail.getDescription());
            result.setPrice(hairDetail.getPrice());
            List<String> images = hairDetail.getHairDetailImages().stream().map(HairDetailImage::getFileUrl)
                    .collect(Collectors.toList());
            result.setImages(images);
            result.setId(hairDetail.getId());
            return result;
        }).collect(Collectors.toList());
    }

    public Optional<HairDetailResponse> getById(Long id) {
        return hairDetailRepository.findById(id).map(hairDetail -> {
            var result = new HairDetailResponse();
            result.setName(hairDetail.getName());
            result.setDescription(hairDetail.getDescription());
            result.setPrice(hairDetail.getPrice());
            List<String> images = hairDetail.getHairDetailImages().stream().map(HairDetailImage::getFileUrl)
                    .collect(Collectors.toList());
            result.setImages(images);
            result.setId(hairDetail.getId());
            return result;
        });
    }
}
