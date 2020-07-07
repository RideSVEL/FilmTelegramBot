package serejka.telegram.bot.repository;

import org.springframework.data.repository.CrudRepository;
import serejka.telegram.bot.models.Review;

import java.util.List;

public interface ReviewRepository extends CrudRepository<Review, Long> {

    List<Review> findAllByView(int view);

}
