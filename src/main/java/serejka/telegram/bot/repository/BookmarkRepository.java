package serejka.telegram.bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import serejka.telegram.bot.models.Bookmark;
import serejka.telegram.bot.models.User;

import java.util.List;
import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    Optional<Bookmark> findBookmarkByUserAndMovieId(User user, Long movieId);

    List<Bookmark> findAllByUser(User user);
}
