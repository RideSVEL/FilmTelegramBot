package serejka.telegram.bot.models;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.search.annotations.*;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.bridge.builtin.IntegerBridge;

import javax.persistence.*;

@Indexed
@Data
@NoArgsConstructor
@Entity
@Table(name = "users")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Field(termVector = TermVector.YES)
    @FieldBridge(impl = IntegerBridge.class)
    @Column(nullable = false)
    Integer userId;
    @Field(termVector = TermVector.YES)
    String userName;
    @Field(termVector = TermVector.YES)
    String firstName;
    @Field(termVector = TermVector.YES)
    String lastName;
    int countOfUse;

    public User(Integer userId, String userName, String firstName, String lastName, int countOfUse) {
        this.userId = userId;
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.countOfUse = countOfUse;
    }

}
