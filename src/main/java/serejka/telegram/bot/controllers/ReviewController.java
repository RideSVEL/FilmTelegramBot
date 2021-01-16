package serejka.telegram.bot.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import serejka.telegram.bot.models.Review;
import serejka.telegram.bot.models.User;
import serejka.telegram.bot.service.ReviewService;
import serejka.telegram.bot.service.UserService;

import java.util.Collections;
import java.util.List;

@Controller
public class ReviewController {

    private final ReviewService reviewService;
    private final UserService userService;

    public ReviewController(ReviewService reviewService, UserService userService) {
        this.reviewService = reviewService;
        this.userService = userService;
    }

    @GetMapping("/reviews/new")
    public String getNewReviews(Model model) {
        List<Review> newReviews = reviewService.getNewReviews();
        Collections.reverse(newReviews);
        model.addAttribute("reviews", newReviews);
        model.addAttribute("activeNew", true);
        model.addAttribute("title", "New reviews");
        return "reviews";
    }

    @GetMapping("/reviews/archive")
    public String getArchiveReviews(Model model) {
        List<Review> archiveReviews = reviewService.getArchiveReviews();
        model.addAttribute("reviews", archiveReviews);
        model.addAttribute("activeArchive", true);
        model.addAttribute("title", "Archive reviews");
        return "reviews";
    }

    @GetMapping("/see/{id}}")
    public String getReview(@PathVariable(value = "id") Long id, Model model) {
        model.addAttribute("id", id);
        Review review = reviewService.getReview(id);
        if (review != null) {
            model.addAttribute("review", review);
            User userByUserId = userService.findUserByUserId(review.getUserId());
            if (userByUserId != null) {
                model.addAttribute("user", userByUserId);
            }
            review.setView(1);
            reviewService.save(review);
        }
        return "details-review";
    }

}
