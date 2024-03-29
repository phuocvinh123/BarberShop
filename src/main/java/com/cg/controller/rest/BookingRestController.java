package com.cg.controller.rest;

import com.cg.exception.DataInputException;
import com.cg.service.bookingService.BookingService;
import com.cg.service.bookingService.bookingRequest.BookingSaveRequest;
import com.cg.service.bookingService.bookingResponse.BookingListResponse;
import com.cg.service.stylistService.StylistService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@AllArgsConstructor
public class BookingRestController {
    private final BookingService bookingService;

    private final StylistService stylistService;

    @GetMapping
    public ResponseEntity<Page<BookingListResponse>> list(@PageableDefault(size = 5) Pageable pageable,
                                                          @RequestParam(defaultValue = "") String search) {
        return new ResponseEntity<>(bookingService.getAll(pageable, search), HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<List<BookingListResponse>> listById(@PathVariable Long id){
        return new ResponseEntity<>(bookingService.getByIdUser(id), HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<List<BookingListResponse>> findByBookingId(@PathVariable Long id) {
        return new ResponseEntity<>(bookingService.getByIdBooking(id), HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<?> create(@RequestBody BookingSaveRequest request){
        var idStylist = request.getStylist().getId();

        if (stylistService.findById(Long.valueOf(idStylist)).getStatus().toString().equals("BUSY")){
            throw new DataInputException("Thợ đang bận!");
        }
        bookingService.create(request);

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/{status}")
    public ResponseEntity<?> changeStatus(@PathVariable Long id, @PathVariable String status) {
        bookingService.changeStatus(id, status);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/{date}")
    public ResponseEntity<List<String>> getTimesBooked(@PathVariable Long id, @PathVariable String date) {
        List<String> timesBooked = bookingService.getTimesStylist(id, date);
        return ResponseEntity.ok(timesBooked);
    }



}
