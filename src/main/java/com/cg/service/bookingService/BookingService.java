package com.cg.service.bookingService;

import com.cg.domain.*;
import com.cg.domain.Enum.EStatusBooking;
import com.cg.repository.*;
import com.cg.service.bookingService.bookingRequest.BookingSaveRequest;
import com.cg.service.bookingService.bookingResponse.BookingListResponse;
import com.cg.utils.AppUtils;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BookingService {
    private final IBookingRepository bookingRepository;

    private final IBookingDetailRepository bookingDetailRepository;

    private final IHairDetailRepository hairDetailRepository;

    private final IHairDetailImageRepository hairDetailImageRepository;

    private final IStylistRepository stylistRepository;

    private final IStylistImageRepository stylistImageRepository;

    private final ICustomerRepository customerRepository;

    private final UserRepository userRepository;

    public Page<BookingListResponse> getAll(Pageable pageable, String search){
        search = "%" + search + "%";
        return bookingRepository.searchEverything(search ,pageable).map(e -> {
            var result = AppUtils.mapper.map(e, BookingListResponse.class);
            result.setStylist(e.getStylist().getName());
            if(e.getCustomer() == null){
                result.setRole(e.getUser().getRole().toString());
            } else {
                result.setRole("Customer");
            }
            result.setBookingDetails(e.getBookingDetails()
                    .stream().map(c -> c.getHairDetail().getName())
                    .collect(Collectors.joining(", ")));
            return result;
        });
    }

    public List<BookingListResponse> getByIdUser(Long id) {
        return bookingRepository.findBookingsByUserId(id).stream().map(e -> {
            var result = AppUtils.mapper.map(e, BookingListResponse.class);
            result.setStylist(e.getStylist().getName());
            if(e.getCustomer() == null){
                result.setRole(e.getUser().getRole().toString());
            } else {
                result.setRole("Customer");
            }
            result.setBookingDetails(e.getBookingDetails()
                    .stream().map(c -> c.getHairDetail().getName())
                    .collect(Collectors.joining(", ")));
            return result;
        }).collect(Collectors.toList());
    }

    public List<BookingListResponse> getByIdBooking(Long id){
        return bookingRepository.findBookingById(id).stream().map(e -> {
            var result = AppUtils.mapper.map(e, BookingListResponse.class);
            result.setStylist(e.getStylist().getName());
            if(e.getCustomer() == null){
                result.setRole(e.getUser().getRole().toString());
            } else {
                result.setRole("Customer");
            }
            result.setBookingDetails(e.getBookingDetails()
                    .stream().map(c -> c.getHairDetail().getName() + "-" + c.getHairDetail().getPrice())
                    .collect(Collectors.joining(", ")));
            return result;
        }).collect(Collectors.toList());
    }


    public void create(BookingSaveRequest request){
        var book = AppUtils.mapper.map(request, Booking.class);
        String dateTimeBooking = request.getDayBooking() +'T'+ request.getTimeBooking();
        LocalDateTime dateTimeBook = LocalDateTime.parse(dateTimeBooking);
        book.setDayTimeBooking(dateTimeBook);
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (var idHairDetail:request.getIdHairDetails()) {
            totalPrice = hairDetailRepository.findById(Long.valueOf(idHairDetail)).get().getPrice().add(totalPrice);
        }
        book.setTotalPrice(totalPrice);
        book.setStatus(EStatusBooking.valueOf("UNPAID"));
        if(request.getIdUser().equals("")){
            var customer = AppUtils.mapper.map(request, Customer.class);
            customerRepository.save(customer);
            book.setCustomer(customer);
        } else {
            Optional<User> user = userRepository.findById(Long.valueOf(request.getIdUser()));
            User userBook = user.get();
            book.setName(userBook.getFullName());
            book.setPhoneNumber(userBook.getPhoneNumber());
            book.setUser(userBook);
        }

        bookingRepository.save(book);
        Booking finalBook = book;
        bookingDetailRepository.saveAll(request
                .getIdHairDetails()
                .stream()
                .map(id -> new BookingDetail(finalBook, new HairDetail(Long.valueOf(id)),request.getName(),hairDetailRepository.findById(Long.valueOf(id)).get().getPrice()))
                .collect(Collectors.toList()));
    }

    public void changeStatus(Long id, String status) {
        var booking = bookingRepository.findById(id).orElse(new Booking());
        booking.setStatus(EStatusBooking.valueOf(status));
        bookingRepository.save(booking);

    }

    public List<String> getTimesStylist(Long id, String date){
        List <String> times = new ArrayList<>();
        var bookings = bookingRepository.findBookingsByStylistId(id);
        for(var booking : bookings){
            var daytime = booking.getDayTimeBooking();
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

            // Lấy day và time dưới dạng chuỗi
            String day = daytime.format(dateFormatter);
            String time = daytime.format(timeFormatter);

            if(day.equals(date)){
                times.add(time);
            }
        }
        return times;
    }


}
