package serejka.telegram.bot.models;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;


@Data
@Entity
@Table(name = "statistics")
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Stats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    int count;
    @Column(unique = true)
    String commandName;

}
