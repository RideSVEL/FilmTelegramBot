package serejka.telegram.bot.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import serejka.telegram.bot.models.User;
import serejka.telegram.bot.service.UserService;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
public class UsersController {

    private final UserService userService;

    @GetMapping("/")
    public String redirect() {
        return "redirect:/users";
    }

    @GetMapping("/users")
    public String showAllUsers(Model model) {
        List<User> allUsers = userService.findAllUsers();
        model.addAttribute("title", "Users");
        model.addAttribute("users", allUsers);
        log.info(allUsers.toString());
        return "users";
    }

}
