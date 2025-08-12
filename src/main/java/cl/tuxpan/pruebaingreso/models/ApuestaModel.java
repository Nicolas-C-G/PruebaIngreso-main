package cl.tuxpan.pruebaingreso.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

/**
 * JPA entity that represents a bet (apuesta) made by a user on an item.
 * <p>
 * Typical fields include:
 * <ul>
 *   <li>{@code id}: primary key</li>
 *   <li>{@code amount}: bet amount</li>
 *   <li>{@code item}: many-to-one relation to {@code ItemModel}</li>
 *   <li>{@code usuario}: many-to-one relation to {@code UsuarioModel}</li>
 * </ul>
 */
@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "subasta_apuesta")
public class ApuestaModel {

  /**
   * Primary key for the bet.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "apuesta_id")
  private Integer id;

  /**
   * The wagered amount for this bet.
   */
  @Column(name = "apuesta_monto")
  private Integer amount;

  /**
   * The user who placed the bet.
   */
  @ManyToOne
  @JoinColumn(name = "apuesta_usuario_id")
  UsuarioModel usuario;

  /**
   * The item on which the bet was placed.
   */
  @ManyToOne
  @JoinColumn(name = "apuesta_item_id")
  ItemModel item;

  /**
   * Compares this bet with another object for equality.
   * <p>
   * Two bets are considered equal if they share the same {@code id}.
   * Uses Hibernate-specific logic to handle proxy instances correctly.
   *
   * @param o the object to compare with
   * @return {@code true} if the objects are equal, {@code false} otherwise
   */
  @Override
  public final boolean equals(Object o) {
    if (this == o) return true;
    if (o == null) return false;
    Class<?> oEffectiveClass = o instanceof HibernateProxy obj ? obj.getHibernateLazyInitializer().getPersistentClass() : o.getClass();
    Class<?> thisEffectiveClass = this instanceof HibernateProxy obj ? obj.getHibernateLazyInitializer().getPersistentClass() : this.getClass();
    if (thisEffectiveClass != oEffectiveClass) return false;
    ApuestaModel apuestaModel = (ApuestaModel) o;
    return getId() != null && Objects.equals(getId(), apuestaModel.getId());
  }

  /**
   * Returns a hash code value for the bet.
   * <p>
   * Uses Hibernate-specific logic to handle proxy instances correctly.
   *
   * @return the hash code for this entity
   */
  @Override
  public final int hashCode() {
    return this instanceof HibernateProxy obj ? obj.getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
  }
}
