package uz.maniac4j.data.entity;

import java.time.LocalDateTime;
import javax.persistence.Entity;

@Entity
public class HistoryOneItem extends AbstractEntity {

    private LocalDateTime date;
    private Integer value;
    private String status;

    public LocalDateTime getDate() {
        return date;
    }
    public void setDate(LocalDateTime date) {
        this.date = date;
    }
    public Integer getValue() {
        return value;
    }
    public void setValue(Integer value) {
        this.value = value;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

}
