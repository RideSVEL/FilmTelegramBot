package serejka.telegram.bot.logic.bot;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import serejka.telegram.bot.service.StatisticsService;
import serejka.telegram.bot.service.UserService;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
public class Util {

    StatisticsService statisticsService;
    UserService userService;

    @Async
    public void updateStatisticsByCommand(Message message) {
        statisticsService.updateStatisticCommand(message);
    }

    @Async
    public void updateCountOfUse(Integer userId) {
        userService.updateByUserIdCountOfUse(userId);
    }

}
