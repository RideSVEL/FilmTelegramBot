package serejka.telegram.bot.controllers;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import serejka.telegram.bot.models.Stats;
import serejka.telegram.bot.resources.StatResource;
import serejka.telegram.bot.service.StatisticsService;

import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
@Controller
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StatsController {

    StatResource statResource;

    @GetMapping("/stat")
    public String showAllUsers(Model model) {
        List<Stats> stats = statResource.statisticsByCommands();
        model.addAttribute("title", "Statistics");
        model.addAttribute("stats", stats);
        return "stats";
    }


}
