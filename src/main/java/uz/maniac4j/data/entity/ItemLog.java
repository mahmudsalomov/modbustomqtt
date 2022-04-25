package uz.maniac4j.data.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.util.Objects;

@Getter
@Setter
@ToString
//@RequiredArgsConstructor
@NoArgsConstructor
@Entity
public class ItemLog extends AbstractEntity{

    private double value;

    @ManyToOne
    private ModbusItem modbusItem;


    @Builder
    public ItemLog(double value, ModbusItem modbusItem) {
        this.value = value;
        this.modbusItem = modbusItem;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ItemLog itemLog = (ItemLog) o;
        return getId() != null && Objects.equals(getId(), itemLog.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
