package com.domingueti.upfine.modules.User.models;

import com.domingueti.upfine.modules.Corporation.models.Corporation;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "tb_user")
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = "deleted_at IS NULL")
@SQLDelete(sql = "update tb_user set deleted_at = current_timestamp where id=?")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private @Getter @Setter Long id;

    private @Getter @Setter String name;

    private @Getter @Setter String email;

    private @Getter @Setter boolean active;

    private @Getter @Setter LocalDate referenceDate;

    @CreationTimestamp
    private @Getter Timestamp createdAt;

    @UpdateTimestamp
    private @Getter Timestamp updatedAt;

    private @Getter @Setter Timestamp deletedAt;

    @ToString.Exclude
    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = "tb_pivot_user_corporation", joinColumns = {
        @JoinColumn(name = "user_id")}, inverseJoinColumns = { @JoinColumn(name = "corporationId")} )
    private @Getter List<Corporation> corporations = new ArrayList<>();

}
