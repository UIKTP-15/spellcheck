package finki_ukim.spell_check_back_end.controller;

import finki_ukim.spell_check_back_end.model.User;
import finki_ukim.spell_check_back_end.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String getRegisterPage() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam(name = "firstName") String firstName,
                               @RequestParam(name = "lastName") String lastName,
                               @RequestParam(name = "email") String email,
                               @RequestParam(name = "password") String password,
                               Model model) {
        try {
            User user = this.userService.registerUser(firstName, lastName, email, password);
            return "redirect:/users/login";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam(name = "email") String email,
                            @RequestParam(name = "password") String password,
                            Model model,
                            HttpSession session) {
        try {
            User user = this.userService.loginUser(email, password);
            session.setAttribute("user", user);
            return "home";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "login";
        }
    }
}
