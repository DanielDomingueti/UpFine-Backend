package com.domingueti.upfine.modules.Ipe.models;

import com.domingueti.upfine.modules.Corporation.models.Corporation;
import com.domingueti.upfine.modules.RelevantFact.models.RelevantFact;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDate;

@Entity(name = "tb_ipe")
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = "deleted_at IS NULL")
@SQLDelete(sql = "update tb_ipe set deleted_at = current_timestamp where id=?")
public class Ipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private @Getter
    @Setter Long id;

    private @Getter @Setter Long corporationId;

    private @Getter @Setter String subject;

    private @Getter @Setter String link;

    private @Getter @Setter LocalDate referenceDate;

    @CreationTimestamp
    private @Getter Timestamp createdAt;

    @UpdateTimestamp
    private @Getter Timestamp updatedAt;

    private @Getter @Setter Timestamp deletedAt;

    @ToString.Exclude
    @ManyToOne(optional = false)
    @JoinColumn(name = "corporationId", insertable = false, updatable = false)
    private @Getter Corporation corporation;

    @ToString.Exclude
    @OneToOne(mappedBy = "ipe", cascade = CascadeType.ALL)
    private @Getter RelevantFact relevantFact;

}