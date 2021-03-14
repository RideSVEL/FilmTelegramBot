package serejka.telegram.bot.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import serejka.telegram.bot.models.Bookmark;
import serejka.telegram.bot.models.Movie;
import serejka.telegram.bot.models.User;
import serejka.telegram.bot.repository.BookmarkRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookmarkService {

    BookmarkRepository bookmarkRepository;
    UserService userService;
    ParserService parserService;

    public String saveBookmark(Integer userId, Long movieId) {
        Optional<Bookmark> bookmark = checkExistBookmark(userId, movieId);
        if (bookmark.isEmpty()) {
            User userByUserId = userService.findUserByUserId(userId);
            bookmarkRepository.save(new Bookmark(userByUserId, movieId));
            return "Фильм был успешно добавлен в закладки\uD83D\uDE4C";
        } else {
            return "Этот фильм уже находится в ваших закладках\uD83D\uDE45\u200D♂️";
        }

    }

    public String deleteBookmark(Integer userId, Long movieId) {
        Optional<Bookmark> bookmark = checkExistBookmark(userId, movieId);
        if (bookmark.isPresent()) {
            bookmarkRepository.delete(bookmark.get());
            return "Фильм был удален из закладок❌";
        } else {
            return "Этот фильм отсутствует в ваших закладках\uD83D\uDE45\u200D♂️";
        }
    }

    public Optional<Bookmark> checkExistBookmark(Integer userId, Long movieId) {
        User userByUserId = userService.findUserByUserId(userId);
        return bookmarkRepository.findBookmarkByUserAndMovieId(userByUserId, movieId);
    }

    public List<Bookmark> findAllBookmarksByUserId(Integer userId) {
        return bookmarkRepository.findAllByUser(userService.findUserByUserId(userId));
    }

    public List<Movie> findAllMoviesByUserBookmarks(Integer userId) {
        List<Movie> movies = new ArrayList<>();
        for (Bookmark bookmark : findAllBookmarksByUserId(userId)) {
            movies.add(parserService.parseMovie(Integer.parseInt(String.valueOf(bookmark.getMovieId()))));
        }
        return movies;
    }

}
