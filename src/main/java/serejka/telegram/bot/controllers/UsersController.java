package serejka.telegram.bot.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import serejka.telegram.bot.models.User;
import serejka.telegram.bot.service.UserService;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class UsersController {

    private final UserService userService;

    @GetMapping("/")
    public String redirect() {
        return "Hello";
    }

    @GetMapping("/users")
    public String showAllUsers(Model model) {
        List<User> allUsers = userService.findAllUsers();
        model.addAttribute("title", "Users");
        model.addAttribute("users", allUsers);
        return "users";
    }

}
