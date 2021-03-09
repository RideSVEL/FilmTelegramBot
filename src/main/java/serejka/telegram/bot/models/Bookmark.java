package serejka.telegram.bot.models;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "bookmarks")
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Bookmark implements Serializable {

    public Bookmark(User user, Long movie_id) {
        this.user = user;
        this.movie_id = movie_id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    Long movie_id;


}
