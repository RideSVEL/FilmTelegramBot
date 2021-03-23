package serejka.telegram.bot.resources;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import serejka.telegram.bot.models.Review;
import serejka.telegram.bot.service.ReviewService;

import java.util.Collections;
import java.util.List;


@RestController
@RequestMapping("/api/review")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ReviewResource {

    ReviewService reviewService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/new")
    public List<Review> getNewReviews() {
        List<Review> newReviews = reviewService.getNewReviews();
        Collections.reverse(newReviews);
        return newReviews;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/archive")
    public List<Review> getArchiveReviews() {
        return reviewService.getArchiveReviews();
    }
}
