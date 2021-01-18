package serejka.telegram.bot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import serejka.telegram.bot.botapi.Commands;
import serejka.telegram.bot.models.Stats;
import serejka.telegram.bot.repository.StatsRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class StatisticsService {

    private final StatsRepository statsRepository;

    public List<Stats> findAllStatsCommand() {
        return statsRepository.findAll();
    }

    public void updateCountCommand(Commands command) {
        Stats stats1;
        if (command != null) {
            Optional<Stats> stats = statsRepository.findByCommandName(command.getCommand());
            stats1 = stats.orElseGet(
                    Stats::new);
            stats1.setCommandName(command.getCommand());
        } else {
            Optional<Stats> byCommandName
                    = statsRepository.findByCommandName(Commands.OTHER.getCommand());
            stats1 = byCommandName.orElseGet(
                    Stats::new);
            stats1.setCommandName(Commands.OTHER.getCommand());
        }
        stats1.setCount(stats1.getCount() + 1);
        statsRepository.save(stats1);
    }
}
