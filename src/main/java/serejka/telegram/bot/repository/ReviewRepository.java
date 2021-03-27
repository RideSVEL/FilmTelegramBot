package serejka.telegram.bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import serejka.telegram.bot.models.Review;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findAllByViewOrderByIdDesc(int view);

    Long countAllByView(int view);

}
