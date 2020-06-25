package serejka.telegram.bot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import serejka.telegram.bot.models.Stats;
import serejka.telegram.bot.repository.StatsRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class StatisticsService {

    private final StatsRepository statsRepository;

    public StatisticsService(StatsRepository statsRepository) {
        this.statsRepository = statsRepository;
    }

    public void updateCountCommand(Integer id) {
        Optional<Stats> stats = statsRepository.findById(id);
        List<Stats> list = new ArrayList<>();
        stats.ifPresent(list::add);
        Stats temp = list.get(0);
        temp.setCount(temp.getCount() + 1);
        statsRepository.save(temp);
    }
}
