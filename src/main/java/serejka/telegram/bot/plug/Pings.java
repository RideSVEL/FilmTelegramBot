package serejka.telegram.bot.plug;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@EnableScheduling
@Slf4j
@Service
public class Pings {

    @Value("${api.request}")
    private String url;

    private final RestTemplate rest;

    public Pings() {
        rest = new RestTemplate();
    }

    @Scheduled(fixedRate = 1200000)
    public void ping() {
        try {
            ResponseEntity<String> forEntity = rest.getForEntity(url, String.class);
            log.info("Get response with status - {}, with body - {} ",
                    forEntity.getStatusCode(), forEntity.getBody());
        } catch (Exception e) {
            log.info("Bad request");
            e.printStackTrace();
        }

    }

}
