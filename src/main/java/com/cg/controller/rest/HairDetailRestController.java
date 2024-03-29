package com.cg.controller.rest;

import com.cg.service.hairDetailService.HairDetailService;
import com.cg.service.hairDetailService.hairDetailRequest.HairDetailSaveRequest;
import com.cg.service.hairDetailService.hairDetailResponse.HairDetailListResponse;
import com.cg.service.hairDetailService.hairDetailResponse.HairDetailResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/hairDetails")
@AllArgsConstructor
public class HairDetailRestController {
    private final HairDetailService hairDetailService;

//    @GetMapping
//    public ResponseEntity<Page<HairDetailListResponse>> getHairDetails(@PageableDefault(size = 5) Pageable pageable){
//        return new ResponseEntity<>(hairDetailService.getHairDetails(pageable), HttpStatus.OK);
//    }

    @GetMapping
    public ResponseEntity<Page<HairDetailListResponse>> getHairDetails(@PageableDefault(size = 5) Pageable pageable,
                                                                       @RequestParam(defaultValue = "") String search){
        return new ResponseEntity<>(hairDetailService.getHairDetails(pageable, search), HttpStatus.OK);
    }

    @PostMapping
    public void create(@RequestBody HairDetailSaveRequest request){
        hairDetailService.create(request);
    }

    @GetMapping("{id}")
    public ResponseEntity<HairDetailResponse> findById(@PathVariable Long id) {
        HairDetailResponse hairDetail = hairDetailService.findById(id);
        return new ResponseEntity<>(hairDetail, HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<?> updateHairDetail(@RequestBody HairDetailSaveRequest request, @PathVariable Long id) {
        hairDetailService.update(request, id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Long id) {
        Boolean isDeleted = hairDetailService.delete(id);
        if (isDeleted) {
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.notFound().build();
        }
    }





}
