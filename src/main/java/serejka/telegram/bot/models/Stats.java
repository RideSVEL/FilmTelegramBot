package serejka.telegram.bot.models;

import javax.persistence.*;

@Entity
@Table(name = "statistics")
public class Stats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer count;
    private String commandName;

    public Stats(Integer id, Integer count, String commandName) {
        this.id = id;
        this.count = count;
        this.commandName = commandName;
    }

    public Stats() {
    }

    public String getCommandName() {
        return commandName;
    }

    public void setCommandName(String commandName) {
        this.commandName = commandName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
