package serejka.telegram.bot.models;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

@Data
@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private Integer userId;
    private String review;
    private String date;
    private int view;

}
