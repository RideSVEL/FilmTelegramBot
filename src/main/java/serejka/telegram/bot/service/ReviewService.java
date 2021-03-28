package serejka.telegram.bot.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import serejka.telegram.bot.logic.enums.Commands;
import serejka.telegram.bot.cache.UserDataCache;
import serejka.telegram.bot.models.Review;
import serejka.telegram.bot.models.User;
import serejka.telegram.bot.repository.ReviewRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReviewService {

    ReviewRepository reviewRepository;
    UserDataCache userDataCache;
    KeyboardService keyboardService;
    UserService userService;
    ReplyToUserService replyToUserService;

    public Review save(Review review) {
        return reviewRepository.save(review);
    }

    public Review getReview(Long id) {
        Optional<Review> byId = reviewRepository.findById(id);
        return byId.orElse(null);
    }

    public void deleteReview(Review review) {
        reviewRepository.delete(review);
    }

    public List<Review> getNewReviews() {
        return reviewRepository.findAllByViewOrderByIdDesc(0);
    }

    public List<Review> getArchiveReviews() {
        return reviewRepository.findAllByViewOrderByIdDesc(1);
    }

    public List<Review> findAllReviews() {
        return reviewRepository.findAll();
    }

    public Long countReviewsByView(Integer view) {
        return reviewRepository.countAllByView(view);
    }

    public SendMessage reviewLogic(Message message) {
        if (message.getText().equals("/cancel")
                || message.getText().equals("Вернуться\uD83D\uDE15")) {
            userDataCache.deleteStateUser(message.getFrom().getId());
            return keyboardService.getKeyboard(message.getChatId(),
                    "Увидимся в следующий раз\uD83D\uDE0A", Commands.START);
        }
        User user = userService.findUserByUserId(message.getFrom().getId());
        String reply;
        if (user != null) {
            Review review = new Review();
            review.setReview(message.getText());
            review.setUserId(message.getFrom().getId());
            review.setDate(new Date().toString());
            try {
                save(review);
                log.info(" <||> Save to DB new review from user: {}", message.getFrom().getId());
                reply = "Братан, спасибо тебе большое за отзыв! Ты лучший!";
                userDataCache.deleteStateUser(message.getFrom().getId());
            } catch (Exception e) {
                reply = "Шось не то, прости пожалуйста((";
                userDataCache.deleteStateUser(message.getFrom().getId());
            }
        } else {
            reply = replyToUserService.replyError();
            userDataCache.deleteStateUser(message.getFrom().getId());
        }
        return keyboardService.getKeyboard(message.getChatId(),
                reply, Commands.OTHER);
    }

}
