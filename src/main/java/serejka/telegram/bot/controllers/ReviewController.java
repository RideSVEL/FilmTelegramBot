package serejka.telegram.bot.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import serejka.telegram.bot.models.Review;
import serejka.telegram.bot.service.ReviewService;

import java.util.List;

@Controller
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/reviews/new")
    public String getNewReviews(Model model) {
        List<Review> newReviews = reviewService.getNewReviews();
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

    @PostMapping("/review/{id}}")
    public String getReview(@PathVariable(value = "id") Long id) {
        return "review-detail";
    }

}
