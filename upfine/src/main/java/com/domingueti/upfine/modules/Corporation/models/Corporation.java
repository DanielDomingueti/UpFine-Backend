package com.domingueti.upfine.modules.Corporation.models;

import com.domingueti.upfine.modules.Ipe.models.Ipe;
import com.domingueti.upfine.modules.User.models.User;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "tb_corporation")
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = "deleted_at IS NULL")
@SQLDelete(sql = "update tb_corporation set deleted_at = current_timestamp where id=?")
public class Corporation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private @Getter @Setter Long id;

    private @Getter @Setter String cnpj;

    private @Getter @Setter String name;

    @CreationTimestamp
    private @Getter Timestamp createdAt;

    @UpdateTimestamp
    private @Getter Timestamp updatedAt;

    private @Getter @Setter Timestamp deletedAt;

    @ToString.Exclude
    @OneToMany(mappedBy = "corporation", cascade = CascadeType.ALL)
    private @Getter List<Ipe> ipes = new ArrayList<>();

    @ToString.Exclude
    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = "tb_pivot_corporation_user", joinColumns = {
            @JoinColumn(name = "corporation_id")}, inverseJoinColumns = { @JoinColumn(name = "userId")} )
    private @Getter List<User> users = new ArrayList<>();
}
