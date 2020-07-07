package serejka.telegram.bot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import serejka.telegram.bot.models.Review;
import serejka.telegram.bot.repository.ReviewRepository;

import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    public void save(Review review) {
        reviewRepository.save(review);
    }

    public List<Review> getNewReviews() {
        return reviewRepository.findAllByView(0);
    }

    public List<Review> getArchiveReviews() {
        return reviewRepository.findAllByView(1);
    }

}
