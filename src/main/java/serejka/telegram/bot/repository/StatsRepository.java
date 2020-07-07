package serejka.telegram.bot.repository;

import org.springframework.data.repository.CrudRepository;
import serejka.telegram.bot.models.Review;
import serejka.telegram.bot.models.Stats;

import java.util.List;

public interface StatsRepository extends CrudRepository<Stats, Integer> {


}
