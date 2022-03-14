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

package io.loanshark.contracts.service.web.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.money.MonetaryAmount;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContractDto {

    @Null
    //@JsonSerialize(using = IdMaskLongSerializers.Serializer.class)
    //@JsonDeserialize(using = IdMaskLongSerializers.Deserializer.class)
    private Long id = null;

    @Null
    private Integer version = null;

    @Null
    private OffsetDateTime createdDate = null;

    @Null
    private OffsetDateTime lastModifiedDate = null;

    @NotNull
    private MonetaryAmount amount;

    private BigDecimal interestRate;

    private String riskRating;

    private Integer tenureInMonths;


}
