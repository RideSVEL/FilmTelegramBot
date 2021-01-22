package serejka.telegram.bot.plug;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

@EnableScheduling
@Slf4j
@Getter
@Service
public class Pings {

    @Value("${api.request}")
    private String url;
    private final String url2 = "https://www.google.com";

    private final RestTemplate rest;

    public Pings() {
        rest = new RestTemplate();
    }

    @Async
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

    @Scheduled(fixedRate = 600000)
    public void pingMe() {
        try {
            URL url = new URL(getUrl2());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            log.info("Ping {}, OK: response code {}", url.getHost(), connection.getResponseCode());
            connection.disconnect();
        } catch (IOException e) {
            log.error("Ping FAILED");
            e.printStackTrace();
        }

    }

}
