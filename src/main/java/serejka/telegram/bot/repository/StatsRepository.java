package serejka.telegram.bot.repository;

import org.springframework.data.repository.CrudRepository;
import serejka.telegram.bot.models.Stats;

public interface StatsRepository extends CrudRepository<Stats, Integer> {

}
