package it.buniva.strage.entity;

import it.buniva.strage.enumaration.PermissionName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="PERMISSION")
public class Permission implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name="PERMISSION_ID")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long permissionId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Size(min=4, max=50)
    private PermissionName permissionName;

    @NotNull
    @Size(min=4, max=50)
    @Column(name="PERMISSION_VALUE")
    private String permissionValue;
}