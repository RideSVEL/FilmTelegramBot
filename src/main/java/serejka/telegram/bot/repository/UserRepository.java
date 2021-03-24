package serejka.telegram.bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import serejka.telegram.bot.models.User;

import java.util.List;


public interface UserRepository extends JpaRepository<User, Long> {


    boolean existsUserByUserId(Integer userId);

    User findUserByUserId(Integer userId);

    @Query(value = "select * from Users u where u.id > ?1 order by u.id limit 10",
            nativeQuery = true)
    List<User> findAllByIdGreaterThanOrderById(Long id);
}
