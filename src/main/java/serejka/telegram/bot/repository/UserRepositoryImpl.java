package serejka.telegram.bot.repository;

import org.springframework.stereotype.Repository;
import serejka.telegram.bot.models.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class UserRepositoryImpl{

    @PersistenceContext
    private EntityManager entityManager;

    public List<User> findOrderedByIdLimitingBy(Long id, Integer limit) {
        return entityManager.createQuery("SELECT u FROM User u where u.id > ?1 ORDER BY u.id",
                User.class).setParameter(1, id).setMaxResults(limit).getResultList();
    }
}
