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
 * JPA entity that represents a user who participates in auctions.
 * <p>
 * Typical fields include:
 * <ul>
 *   <li>{@code id}: primary key</li>
 *   <li>{@code name}: user display name</li>
 *   <li>{@code apuestas}: one-to-many relation with {@code ApuestaModel}</li>
 * </ul>
 */

@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "subasta_usuario")
public class UsuarioModel {
  /**
   * Primary key for the user.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "usuario_id")
  private Integer id;

  /**
   * Display name of the user.
   */
  @Column(name = "usuario_nombre")
  private String name;

  /**
   * List of bets placed by this user.
   */
  @OneToMany(mappedBy = "usuario")
  private List<ApuestaModel> apuestas;

  /**
   * Compares this user with another object for equality.
   * <p>
   * Two users are considered equal if they share the same {@code id}.
   * Uses Hibernate-specific logic to handle proxy instances correctly.
   *
   * @param o the object to compare with
   * @return {@code true} if the objects are equal, {@code false} otherwise
   */
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
    UsuarioModel that = (UsuarioModel) o;
    return getId() != null && Objects.equals(getId(), that.getId());
  }

  /**
   * Returns a hash code value for the user.
   * <p>
   * Uses Hibernate-specific logic to handle proxy instances correctly.
   *
   * @return the hash code for this entity
   */
  @Override
  public final int hashCode() {
    return this instanceof HibernateProxy obj
        ? obj.getHibernateLazyInitializer().getPersistentClass().hashCode()
        : getClass().hashCode();
  }
}
