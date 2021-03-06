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

package guru.sfg.brewery.order.service.services;

import guru.sfg.brewery.order.service.web.model.ContractOrderDto;
import guru.sfg.brewery.order.service.web.model.ContractOrderPagedList;
import org.springframework.data.domain.Pageable;

import java.util.Long;

public interface ContractOrderService {
    ContractOrderPagedList listOrders(Long customerId, Pageable pageable);

    ContractOrderDto placeOrder(Long customerId, ContractOrderDto contractOrderDto);

    ContractOrderDto getOrderById(Long customerId, Long orderId);

    void pickupOrder(Long customerId, Long orderId);
}
