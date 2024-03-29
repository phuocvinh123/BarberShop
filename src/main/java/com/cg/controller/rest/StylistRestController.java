package com.cg.controller.rest;


import com.cg.service.stylistService.StylistService;
import com.cg.service.stylistService.stylistRequest.StylistSaveRequest;
import com.cg.service.stylistService.stylistResponse.StylistDetailResponse;
import com.cg.service.stylistService.stylistResponse.StylistListResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/stylists")
@AllArgsConstructor
public class StylistRestController {
    private final StylistService stylistService;



    @GetMapping
    public ResponseEntity<Page<StylistListResponse>> getHairDetails(@PageableDefault(size = 5) Pageable pageable,
                                                                    @RequestParam(defaultValue = "") String search){
        return new ResponseEntity<>(stylistService.getStylists(pageable, search), HttpStatus.OK);
    }

    @PostMapping
    public void create(@RequestBody StylistSaveRequest request){
        stylistService.create(request);
    }

    @GetMapping("{id}")
    public ResponseEntity<StylistDetailResponse> findById(@PathVariable Long id) {
        StylistDetailResponse stylist = stylistService.findById(id);
        return new ResponseEntity<>(stylist, HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<?> updateHairDetail(@RequestBody StylistSaveRequest request, @PathVariable Long id) {
        stylistService.update(request, id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Long id) {
        Boolean isDeleted = stylistService.delete(id);
        if (isDeleted) {
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/{status}")
    public ResponseEntity<?> changeStatus(@PathVariable Long id, @PathVariable String status) {
        stylistService.changeStatus(id, status);
        return ResponseEntity.ok().build();
    }

}
