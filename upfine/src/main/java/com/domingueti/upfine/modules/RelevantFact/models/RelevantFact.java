package com.domingueti.upfine.modules.RelevantFact.models;

import com.domingueti.upfine.modules.Ipe.models.Ipe;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity(name = "tb_relevant_fact")
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = "deleted_at IS NULL")
@SQLDelete(sql = "update tb_relevant_fact set deleted_at = current_timestamp where id=?")
public class RelevantFact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private @Getter
    @Setter Long id;

    private @Getter @Setter Long ipeId;

    private @Getter @Setter String rawText;

    private @Getter @Setter String optimizedText;

    @CreationTimestamp
    private @Getter Timestamp createdAt;

    @UpdateTimestamp
    private @Getter Timestamp updatedAt;

    private @Getter @Setter Timestamp deletedAt;

    @ToString.Exclude
    @OneToOne(optional = false)
    @JoinColumn(name = "ipeId", insertable = false, updatable = false)
    private @Getter Ipe ipe;

}