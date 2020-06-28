package serejka.telegram.bot.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import serejka.telegram.bot.models.User;
import serejka.telegram.bot.service.UserService;

import java.util.List;

@Controller
public class UsersController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String redirect() {
        return "redirect:/users";
    }

    @GetMapping("/users")
    public String showAllUsers(Model model) {
        List<User> allUsers = userService.findAllUsers();
        model.addAttribute("title", "Users");
        model.addAttribute("users", allUsers);
        return "users";
    }

}
