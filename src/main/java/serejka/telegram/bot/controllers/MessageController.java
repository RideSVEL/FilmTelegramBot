package serejka.telegram.bot.controllers;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import serejka.telegram.bot.resources.MessageResource;

@RequiredArgsConstructor
@Controller
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MessageController {

    MessageResource messageResource;

    @GetMapping("/message/{id}")
    public String customMessageToUser(@PathVariable(value = "id") Long id, Model model) {
        model.addAttribute("title", "Send message");
        model.addAttribute("id", id);
        return "message";
    }

    @PostMapping("/message/{id}")
    public String customMessageToUser(@PathVariable(value = "id") Long id, @RequestParam String message) {
        messageResource.customMessageToUser(id, message);
        return "redirect:/users";
    }

    @GetMapping("/message")
    public String messageToAllUsers(Model model) {
        model.addAttribute("title", "Send message");
        return "message";
    }

    @PostMapping("/message")
    public String messageToAllUsers(@RequestParam String message) {
        messageResource.customMessageToAllUsers(message);
        return "redirect:/users";
    }

}
