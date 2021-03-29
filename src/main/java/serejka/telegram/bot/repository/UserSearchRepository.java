package serejka.telegram.bot.repository;

import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.stereotype.Repository;
import serejka.telegram.bot.models.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class UserSearchRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public List<User> searchUsers(String text) {
        FullTextEntityManager fullTextEntityManager =
                Search.getFullTextEntityManager(entityManager);
        QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory()
                .buildQueryBuilder().forEntity(User.class).get();

        Query query = queryBuilder
                .keyword()
                .wildcard()
                .onFields("userName", "firstName", "lastName", "userId")
                .matching("*" + text + "*")
                .createQuery();

        FullTextQuery fullTextQuery =
                fullTextEntityManager.createFullTextQuery(query, User.class);

        return (List<User>) fullTextQuery.getResultList();
    }
}
