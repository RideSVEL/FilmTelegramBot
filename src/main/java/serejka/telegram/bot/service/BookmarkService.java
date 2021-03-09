package serejka.telegram.bot.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import serejka.telegram.bot.models.Bookmark;
import serejka.telegram.bot.models.User;
import serejka.telegram.bot.repository.BookmarkRepository;

@RequiredArgsConstructor
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookmarkService {

    BookmarkRepository bookmarkRepository;
    UserService userService;

    public String saveBookmark(Integer userId, Long movieId) {
        User userByUserId = userService.findUserByUserId(userId);
        bookmarkRepository.save(new Bookmark(userByUserId, movieId));
        return "Фильм был успешно добавлен в закладки\uD83D\uDE4C";
    }

}
