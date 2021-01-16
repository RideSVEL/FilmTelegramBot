package serejka.telegram.bot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import serejka.telegram.bot.models.Review;
import serejka.telegram.bot.repository.ReviewRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public void save(Review review) {
        reviewRepository.save(review);
    }

    public List<Review> getNewReviews() {
        return reviewRepository.findAllByView(0);
    }

    public Review getReview(Long id) {
        Optional<Review> byId = reviewRepository.findById(id);
        return byId.orElse(null);
    }

    public List<Review> getArchiveReviews() {
        return reviewRepository.findAllByView(1);
    }

}
