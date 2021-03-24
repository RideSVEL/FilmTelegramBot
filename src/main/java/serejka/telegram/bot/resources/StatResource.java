package serejka.telegram.bot.resources;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import serejka.telegram.bot.models.Stats;
import serejka.telegram.bot.service.StatisticsService;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class StatResource {

    StatisticsService statisticsService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Stats> statisticsByCommands() {
        return statisticsService.findAllStatsCommand().stream()
                .sorted((Comparator.comparingInt(Stats::getCount)))
                .collect(Collectors.toList());
    }
}
