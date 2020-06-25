package serejka.telegram.bot.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UsersController {

    @GetMapping("/users")
    public String showAllUsers(Model model) {

        return "users";
    }

}
