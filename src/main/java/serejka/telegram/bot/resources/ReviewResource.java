package serejka.telegram.bot.resources;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import serejka.telegram.bot.models.Review;
import serejka.telegram.bot.service.ReviewService;

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
        return reviewService.getNewReviews();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/archive")
    public List<Review> getArchiveReviews() {
        return reviewService.getArchiveReviews();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Review> findAllReviews() {
        return reviewService.findAllReviews();
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Review updateReview(@RequestBody Review review) {
        return reviewService.save(review);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/count")
    public Long countReviews(@RequestParam Integer view) {
        return reviewService.countReviewsByView(view);
    }
}
