/*
 *  Copyright 2019 the original author or authors.
 *
 * This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package io.loanshark.contracts.service.domain;

import io.loanshark.contracts.service.utils.PersistentMoneyAmountAndCurrency;
import lombok.*;
import org.hibernate.annotations.*;

import javax.money.MonetaryAmount;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Created by jt on 2019-01-26.
 */
@Getter
@Setter
@Entity
@Table(schema = "contracts")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TypeDef(name = "money", typeClass = PersistentMoneyAmountAndCurrency.class)
public class Contract {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Version
    private Long version;

    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp createdDate;

    @UpdateTimestamp
    private Timestamp lastModifiedDate;

    private Long accountId;

    @Columns(columns = {@Column(name = "amount_currency", length = 3), @Column(name = "amount_value", precision = 4, scale = 2)})
    @Type(type = "money")
    private MonetaryAmount amount;

    private BigDecimal interestRate;

    private String riskRating;

    private Integer tenureInMonths;

}
