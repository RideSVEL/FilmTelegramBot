package serejka.telegram.bot.resources;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import serejka.telegram.bot.models.User;
import serejka.telegram.bot.service.UserService;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UserResource {

    UserService userService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<User> getAllUsers() {
        return userService.findAllUsers().stream()
                .sorted((Comparator.comparingLong(User::getId)))
                .collect(Collectors.toList());
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/{id}")
    public List<User> getAllUsersWhereIdUsersGreaterThanId(
            @PathVariable Long id, @RequestParam(value = "limit", required = false) Integer limit) {
        if (limit == null || limit == 0) {
            limit = 10;
        }
        return userService.findUsersByGreaterId(id, limit);
    }

}
