package com.cg.controller;


import com.cg.domain.User;
import com.cg.repository.UserRepository;
import com.cg.service.bookingService.BookingService;
import com.cg.service.hairDetailService.HairDetailService;
import com.cg.service.stylistService.StylistService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collection;

@Controller
@AllArgsConstructor
public class HomeController {
    private final HairDetailService hairDetailService;

    private final UserRepository userRepository;

    private final StylistService stylistService;

    private final BookingService bookingService;

    @GetMapping("/home")
    public ModelAndView showHomePage(Model model, Authentication authentication) {
        ModelAndView view = new ModelAndView("views/index");
        view.addObject("stylists", stylistService.getAll());
        view.addObject("hairDetails", hairDetailService.getAll());

        if(authentication == null){
            return view;
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        User user = userRepository.findByUsername(username);
        view.addObject("user",user);

        // Kiểm tra vai trò và thêm vào model nếu cần
        if (userDetails.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"))) {
            model.addAttribute("isAdmin", true);
        } else {
            model.addAttribute("isUser",true);
        }
        return view;
    }


    @GetMapping("/booking")
    public ModelAndView showBookingPage(Authentication authentication) {

        ModelAndView view = new ModelAndView("views/booking");
        view.addObject("hairDetails", hairDetailService.findAll());
        view.addObject("stylists", stylistService.findAll());
        if(authentication == null){
            return view;
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        User user = userRepository.findByUsername(username);
        view.addObject("user",user);

        // Kiểm tra vai trò và thêm vào model nếu cần
        if (userDetails.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"))) {
            view.addObject("isAdmin", true);
        } else {
            view.addObject("isUser",true);
        }

        return view;
    }


    @GetMapping("services")
    public ModelAndView showServicePage(Model model, Authentication authentication) {
        ModelAndView view = new ModelAndView("views/services");
        view.addObject("hairDetails", hairDetailService.getAll());

        if(authentication == null){
            return view;
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        User user = userRepository.findByUsername(username);
        view.addObject("user",user);

        // Kiểm tra vai trò và thêm vào model nếu cần
        if (userDetails.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"))) {
            model.addAttribute("isAdmin", true);
        } else {
            model.addAttribute("isUser",true);
        }
        return view;
    }

    @GetMapping("/service/{id}")
    public ModelAndView showServiceDetail(Model model, @PathVariable Long id, Authentication authentication) {
        ModelAndView view = new ModelAndView("views/servicesDetail");
        view.addObject("hairDetail", hairDetailService.getById(id).get());

        if(authentication == null){
            return view;
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        User user = userRepository.findByUsername(username);
        view.addObject("user",user);

        // Kiểm tra vai trò và thêm vào model nếu cần
        if (userDetails.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"))) {
            model.addAttribute("isAdmin", true);
        } else {
            model.addAttribute("isUser",true);
        }
        return view;
    }

    @GetMapping("portfolio")
    public ModelAndView showPortfolioPage(Model model, Authentication authentication) {
        ModelAndView view = new ModelAndView("views/portfolio");
        view.addObject("stylists", stylistService.getAll());

        if(authentication == null){
            return view;
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        User user = userRepository.findByUsername(username);
        view.addObject("user",user);

        // Kiểm tra vai trò và thêm vào model nếu cần
        if (userDetails.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"))) {
            model.addAttribute("isAdmin", true);
        } else {
            model.addAttribute("isUser",true);
        }
        return view;

    }

    @GetMapping("/history/{id}")
    public ModelAndView showHistoryPage(Model model, Authentication authentication, @PathVariable Long id) {
        ModelAndView view = new ModelAndView("views/history");

        if(authentication == null){
            return view;
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        User user = userRepository.findByUsername(username);
        view.addObject("user",user);

        // Kiểm tra vai trò và thêm vào model nếu cần
        if (userDetails.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"))) {
            model.addAttribute("isAdmin", true);
        } else {
            model.addAttribute("isUser",true);
        }

        return view;
    }


    @GetMapping("/admin")
    public String showAdminPage(Model model, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        model.addAttribute("username", username);
        model.addAttribute("isAdmin",true);
        return "admin/index";
    }



    @GetMapping("/stylist")
    public String showStylistPage(Model model,Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        model.addAttribute("username", username);
        model.addAttribute("isAdmin",true);
        return "admin/stylist";

    }


    @GetMapping("/serviceHair")
    public String showServiceHairPage(Model model, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        model.addAttribute("username", username);
        model.addAttribute("isAdmin",true);
        return "admin/service";

    }

    @GetMapping("/default")
    public String defaultAfterLogin(Authentication authentication) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        if (authorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            return "redirect:/admin"; // Điều hướng đến trang admin nếu có vai trò ADMIN
        } else if (authorities.contains(new SimpleGrantedAuthority("ROLE_USER"))) {
            return "redirect:/home"; // Điều hướng đến trang user nếu có vai trò USER
        } else {
            throw new IllegalStateException("Vai trò không hợp lệ.");
        }
    }

    @GetMapping("403")
    public String error403(){
        return "error403";
    }

    @GetMapping("errors")
    public String errors() {
        return "errors";
    }




}
