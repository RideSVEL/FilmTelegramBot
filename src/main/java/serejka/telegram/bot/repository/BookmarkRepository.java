package serejka.telegram.bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import serejka.telegram.bot.models.Bookmark;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

}
