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
import guru.sfg.brewery.order.service.web.model.ContractOrderLineDto;
import guru.sfg.brewery.order.service.web.model.ContractOrderPagedList;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@ComponentScan(basePackages = {"guru.sfg.brewery.order.service.services", "guru.sfg.brewery.order.service.web.mappers"})
class ContractOrderServiceImplTest extends BaseServiceTest {

    @MockBean
    ContractService contractService;

    @Test
    void listOrders() {

        //make sure we have two orders
        assertThat(contractOrderRepository.count()).isEqualTo(3L);

        ContractOrderPagedList pagedList = contractOrderService.listOrders(testCustomer.getId(), PageRequest.of(0, 25));

        assertThat(pagedList.getTotalElements()).isEqualTo(3L);
        assertThat(pagedList.getContent().size()).isEqualTo(3);
    }

    @Test
    void placeOrder() {
        ContractOrderDto dto = ContractOrderDto.builder()
                .orderStatusCallbackUrl("http://foo.com")
                .contractOrderLines(Arrays.asList(ContractOrderLineDto
                        .builder().contractId(testContractGalaxy.getId()).orderQuantity(12).build()))
                .build();

        ContractOrderDto placedOrder = contractOrderService.placeOrder(testCustomer.getId(), dto);

        assertThat(placedOrder.getId()).isNotNull();
        assertThat(placedOrder.getOrderStatus().name()).isEqualToIgnoringCase("NEW");
    }

    @Test
    void getOrderById() {
        ContractOrderDto dto = contractOrderService.getOrderById(testCustomer.getId(), testOrder1.getId());

        assertThat(dto.getId()).isEqualTo(testOrder1.getId());
    }

    @Test
    void pickupOrder() {
        contractOrderService.pickupOrder(testCustomer.getId(), testOrder1.getId());

        ContractOrderDto dto = contractOrderService.getOrderById(testCustomer.getId(), testOrder1.getId());

        assertThat(dto.getId()).isEqualTo(testOrder1.getId());
        assertThat(dto.getOrderStatus().name()).isEqualTo("PICKED_UP");
    }
}