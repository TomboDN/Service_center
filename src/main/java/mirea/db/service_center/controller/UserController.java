package mirea.db.service_center.controller;

import lombok.RequiredArgsConstructor;
import mirea.db.service_center.model.Customer;
import mirea.db.service_center.model.User;
import mirea.db.service_center.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/register")
    public String getRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "signup_form";
    }

    @PostMapping("/register")
    public String register(User user, Model model) {
        userService.register(user);
        model.addAttribute("customer", new Customer(user));
        return "update_customer_data_form";
    }

    @PostMapping("/customer-data")
    public String updateCustomerData(Customer customer) {
        userService.updateCustomerData(customer);
        return "customer_data_update_success";
    }

}