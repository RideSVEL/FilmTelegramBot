package serejka.telegram.bot.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static org.apache.http.HttpStatus.*;

@Api(tags = "Login resource")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/login")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class LoginResource {

    @ResponseStatus(HttpStatus.ACCEPTED)
    @PostMapping
    @ApiOperation("Check user credentials")
    @ApiResponses({
            @ApiResponse(code = SC_FORBIDDEN, message = "Access denied"),
            @ApiResponse(code = SC_UNAUTHORIZED, message = "Invalid credentials or empty"),
            @ApiResponse(code = SC_INTERNAL_SERVER_ERROR, message = "Something went wrong")
    })
    public void login() {
        log.info("Auth done");
    }
}
