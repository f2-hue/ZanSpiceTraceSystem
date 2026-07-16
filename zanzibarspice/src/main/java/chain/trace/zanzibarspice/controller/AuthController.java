package chain.trace.zanzibarspice.controller;

import chain.trace.zanzibarspice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    @GetMapping("/")
    public String home() {
        return "index";
    }
    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("roles", new String[]{"FARMER", "TRADER"});
        return "auth/register";
    }

    @PostMapping("/register")
    public String processRegister(
            @RequestParam String fullName,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String role,
            Model model) {

        if (userService.emailExists(email)) {
            model.addAttribute("error", "Email already registered");
            model.addAttribute("roles", new String[]{"FARMER", "TRADER"});
            return "auth/register";
        }

        userService.registerUser(fullName, email, password, role);
        return "redirect:/login?registered=true";
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "redirect:/dashboard/home";
    }

    @GetMapping("/dashboard/home")
    public String dashboardHome() {
        return "dashboard";
    }
}