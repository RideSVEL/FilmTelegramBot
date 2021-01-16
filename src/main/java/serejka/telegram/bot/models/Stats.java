package serejka.telegram.bot.models;

import lombok.*;

import javax.persistence.*;


@Data
@Entity
@Table(name = "statistics")
@NoArgsConstructor
public class Stats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private int count;
    @Column(unique = true)
    private String commandName;

}
