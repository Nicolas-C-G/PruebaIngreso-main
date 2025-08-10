package cl.tuxpan.pruebaingreso.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;

import java.util.List;
import java.util.Objects;

/**
 * JPA entity that represents an auction item.
 * <p>
 * Typical fields include:
 * <ul>
 *   <li>{@code id}: primary key</li>
 *   <li>{@code name}: display name of the item</li>
 *   <li>{@code apuestas}: one-to-many relation with {@code ApuestaModel}</li>
 * </ul>
 */

@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "subasta_item")
public class ItemModel {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "item_id")
  private Integer id;

  @Column(name = "item_nombre")
  private String name;

  @OneToMany(mappedBy = "item")
  private List<ApuestaModel> apuestas;

  @Override
  public final boolean equals(Object o) {
    if (this == o) return true;
    if (o == null) return false;
    Class<?> oEffectiveClass =
        o instanceof HibernateProxy obj
            ? obj.getHibernateLazyInitializer().getPersistentClass()
            : o.getClass();
    Class<?> thisEffectiveClass =
        this instanceof HibernateProxy obj
            ? obj.getHibernateLazyInitializer().getPersistentClass()
            : this.getClass();
    if (thisEffectiveClass != oEffectiveClass) return false;
    ItemModel itemModel = (ItemModel) o;
    return getId() != null && Objects.equals(getId(), itemModel.getId());
  }

  @Override
  public final int hashCode() {
    return this instanceof HibernateProxy obj
        ? obj.getHibernateLazyInitializer().getPersistentClass().hashCode()
        : getClass().hashCode();
  }
}
