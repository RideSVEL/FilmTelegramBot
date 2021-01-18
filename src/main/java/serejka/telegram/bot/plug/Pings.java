package serejka.telegram.bot.plug;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

@Slf4j
@Data
@Service
public class Pings {

    @Value("${google.com}")
    private String url;

    @Scheduled(cron = "0 20 * * * *")
    public void ping() throws IOException {
        log.info("ping ping ping");
        URL url = new URL(getUrl());
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.connect();
        log.info("Ping {}, OK - response code {}", url.getHost(), connection.getResponseCode());
        connection.disconnect();
    }

}
