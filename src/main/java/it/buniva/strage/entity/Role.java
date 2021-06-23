package it.buniva.strage.entity;

import it.buniva.strage.enumaration.RoleName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="ROLE")
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name="ROLE_ID")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long roleId;

    @NotNull
    @Column(name="ROLE_NAME")
    @Enumerated(EnumType.STRING)
    @Size(min=4, max=20)
    private RoleName roleName;


    // ============= MAPPING WITH OTHER TABLES =================
    @ManyToMany
    @JoinTable(name="ROLE_PERMISSIONS",
            joinColumns = {@JoinColumn(name="ROLE_ID", referencedColumnName="ROLE_ID", foreignKey = @ForeignKey(name = "FK_ROLE_IN_ROLE_PERMISSION"))},
            inverseJoinColumns = {@JoinColumn(name="PERMISSION_ID", referencedColumnName="PERMISSION_ID", foreignKey = @ForeignKey(name = "FK_PERMISSION_IN_ROLE_PERMISSION"))})
    private Set<Permission> permissionSet;
}
