package serejka.telegram.bot.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import serejka.telegram.bot.service.CustomMessageService;

@Controller
public class MessageController {

    private final CustomMessageService customMessageService;

    public MessageController(CustomMessageService customMessageService) {
        this.customMessageService = customMessageService;
    }

    @GetMapping("/message/{id}")
    public String customMessageToUser(@PathVariable(value = "id") Long id, Model model) {
        model.addAttribute("id", id);
        return "message";
    }

    @PostMapping("/message/{id}")
    public String customMessageToUser(@PathVariable(value = "id") Long id, @RequestParam String message) {
        customMessageService.sendCustomMessageToUser(id, message);
        return "redirect:/users";
    }

    @GetMapping("/message")
    public String messageToAllUsers() {
        return "message";
    }

    @PostMapping("/message")
    public String messageToAllUsers(@RequestParam String message) {
        customMessageService.messageToAllUsers(message);
        return "redirect:/users";
    }

}
