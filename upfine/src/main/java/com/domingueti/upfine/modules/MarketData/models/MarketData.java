package com.domingueti.upfine.modules.MarketData.models;

import com.domingueti.upfine.modules.Corporation.models.Corporation;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;

@Entity(name = "tb_market_data")
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = "deleted_at IS NULL")
@SQLDelete(sql = "update tb_market_data set deleted_at = current_timestamp where id=?")
public class MarketData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private @Getter
    @Setter Long id;

    private @Getter @Setter Long corporationId;

    private @Getter @Setter BigDecimal price;

    private @Getter @Setter Double pl;

    private @Getter @Setter Double pvp;

    private @Getter @Setter Double divYield;

    private @Getter @Setter Double lpa;

    private @Getter @Setter Double vpa;

    private @Getter @Setter Double netMargin;

    private @Getter @Setter Double roe;

    private @Getter @Setter LocalDate lastReferenceDate;

    @CreationTimestamp
    private @Getter Timestamp createdAt;

    @UpdateTimestamp
    private @Getter Timestamp updatedAt;

    private @Getter @Setter Timestamp deletedAt;

    @ToString.Exclude
    @ManyToOne(optional = false)
    @JoinColumn(name = "corporationId", insertable = false, updatable = false)
    private @Getter Corporation corporation;
}