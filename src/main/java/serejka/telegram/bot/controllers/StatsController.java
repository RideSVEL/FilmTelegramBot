package serejka.telegram.bot.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import serejka.telegram.bot.models.Stats;
import serejka.telegram.bot.service.StatisticsService;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class StatsController {

    private final StatisticsService statisticsService;

    @GetMapping("/stat")
    public String showAllUsers(Model model) {
        List<Stats> statsCommand = statisticsService.findAllStatsCommand();
        model.addAttribute("title", "Statistics");
        model.addAttribute("stats", statsCommand);
        return "stats";
    }


}
