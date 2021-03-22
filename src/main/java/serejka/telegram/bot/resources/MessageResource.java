package serejka.telegram.bot.resources;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import serejka.telegram.bot.service.CustomMessageService;

@CrossOrigin
@RestController
@RequestMapping("/api/message")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class MessageResource {

    CustomMessageService customMessageService;

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/{id}")
    public void customMessageToUser(@PathVariable(value = "id") Long id,
                                    @RequestBody String message) {
        customMessageService.sendCustomMessageToUser(id, message);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public void customMessageToAllUsers(@RequestBody String message) {
        customMessageService.messageToAllUsers(message);
    }
}
