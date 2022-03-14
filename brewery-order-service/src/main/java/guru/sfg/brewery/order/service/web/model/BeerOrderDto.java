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

package guru.sfg.brewery.order.service.web.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import guru.sfg.brewery.order.service.domain.OrderStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Long;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContractOrderDto {

    @Null
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id = null;

    @Null
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer version = null;

    @Null
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ssZ", shape=JsonFormat.Shape.STRING)
    private OffsetDateTime createdDate = null;

    @Null
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ssZ", shape=JsonFormat.Shape.STRING)
    private OffsetDateTime lastModifiedDate = null;

    @NotBlank
    private Long customerId;

    private String customerRef;
    private List<ContractOrderLineDto> contractOrderLines;

    @Null
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private OrderStatusEnum orderStatus;
    private String orderStatusCallbackUrl;
}
