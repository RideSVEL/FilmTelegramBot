package serejka.telegram.bot.resources;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.search.bridge.builtin.IntegerBridge;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import serejka.telegram.bot.models.User;
import serejka.telegram.bot.repository.UserSearchRepository;
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
    UserSearchRepository userSearchRepository;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<User> getAllUsers() {
        return userService.findAllUsers().stream()
                .sorted((Comparator.comparingLong(User::getId)))
                .collect(Collectors.toList());
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/{page}/{count}/{sort}/{direction}")
    public List<User> usersPageable(
            @PathVariable Integer page,
            @PathVariable Integer count,
            @PathVariable String sort,
            @PathVariable Integer direction) {
        return userService.findAllUsersPageable(page, count, sort, direction);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/count")
    public Long countUsers() {
        return userService.countAllUsers();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/search")
    public List<User> searchUsers(@RequestParam String searchText) {
        return userSearchRepository.searchUsers(searchText);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/{id}")
    public User findUserByUserId(@PathVariable Integer id) {
        return userService.findUserByUserId(id);
    }

}
