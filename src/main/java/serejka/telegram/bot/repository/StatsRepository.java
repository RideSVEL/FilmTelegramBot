package serejka.telegram.bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import serejka.telegram.bot.models.Review;
import serejka.telegram.bot.models.Stats;

import java.util.List;
import java.util.Optional;

public interface StatsRepository extends JpaRepository<Stats, Integer> {

    Optional<Stats> findByCommandName(String name);

}
