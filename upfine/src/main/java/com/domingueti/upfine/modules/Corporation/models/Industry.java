package com.domingueti.upfine.modules.Corporation.models;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "tb_industry")
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = "deleted_at IS NULL")
@SQLDelete(sql = "update tb_industry set deleted_at = current_timestamp where id=?")
public class Industry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private @Getter
    @Setter Long id;

    private @Getter @Setter String type;

    private @Getter @Setter String description;

    @CreationTimestamp
    private @Getter Timestamp createdAt;

    @UpdateTimestamp
    private @Getter Timestamp updatedAt;

    private @Getter @Setter Timestamp deletedAt;

    @ToString.Exclude
    @OneToMany(mappedBy = "industry", cascade = CascadeType.ALL)
    private @Getter List<Corporation> corporations = new ArrayList<>();
}
