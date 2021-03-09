package serejka.telegram.bot.cache;

import org.springframework.stereotype.Component;
import serejka.telegram.bot.logic.enums.BotState;

import java.util.HashMap;
import java.util.Map;

@Component
public class UserDataCache {

    private final Map<Long, BotState> botStateMap = new HashMap<>();

    public void setUserState(long userId, BotState botState) {
        botStateMap.put(userId, botState);
    }

    public BotState getUserBotState(long userId) {
        return botStateMap.get(userId);
    }

    public void deleteStateUser(long userId) {
        botStateMap.remove(userId);
    }

    public boolean checkContainsKey(long userId) {
        return botStateMap.containsKey(userId);
    }
}
