package serejka.telegram.bot.repository;

import org.springframework.data.repository.CrudRepository;
import serejka.telegram.bot.models.User;


public interface UserRepository extends CrudRepository<User, Long> {

    boolean existsUserByUserId(Integer userId);

    User findUserByUserId(Integer userId);
}
