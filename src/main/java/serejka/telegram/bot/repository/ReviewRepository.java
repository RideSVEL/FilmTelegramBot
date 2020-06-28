package serejka.telegram.bot.repository;

import org.springframework.data.repository.CrudRepository;
import serejka.telegram.bot.models.Review;

public interface ReviewRepository extends CrudRepository<Review, Long> {
}
