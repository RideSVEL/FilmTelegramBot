package serejka.telegram.bot.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

@Slf4j
@RestController
public class DatabaseSenderController {

    @GetMapping(value = "/database", produces = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody()
    public byte[] getDatabase() throws IOException {
        byte[] array = Files.readAllBytes(Paths.get("database/telegram-copy.mv.db"));
        log.info(Arrays.toString(array));
        return array;
    }


}
