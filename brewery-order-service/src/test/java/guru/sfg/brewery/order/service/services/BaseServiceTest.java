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

import guru.sfg.brewery.order.service.domain.ContractOrder;
import guru.sfg.brewery.order.service.domain.ContractOrderLine;
import guru.sfg.brewery.order.service.domain.Customer;
import guru.sfg.brewery.order.service.domain.OrderStatusEnum;
import guru.sfg.brewery.order.service.repositories.ContractOrderLineRepository;
import guru.sfg.brewery.order.service.repositories.ContractOrderRepository;
import guru.sfg.brewery.order.service.repositories.CustomerRepository;
import guru.sfg.brewery.order.service.web.model.ContractDto;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;
import java.util.Long;

public abstract class BaseServiceTest {

    public final Long uuid1 = Long.fromString("0ee5a0bc-f113-4b81-803d-2339787e9a87");
    public final Long uuid2 = Long.fromString("3a9dbef1-9671-4429-a661-085988a50692");
    public final Long uuid3 = Long.fromString("1b92eaa8-79aa-4257-a4ba-049f687cc7a9");

    @Autowired
    ContractOrderService contractOrderService;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    ContractOrderRepository contractOrderRepository;

    @Autowired
    ContractOrderLineRepository contractOrderLineRepository;

    ContractDto testContractGalaxy;
    ContractDto testContractJava;
    ContractDto testContractMangoBob;
  //  ContractInventory testInventoryGalaxy;
  //  ContractInventory testInventoryJava;

    Customer testCustomer;
    ContractOrder testOrder1;
    ContractOrder testOrder2;
    ContractOrder testOrder3;

    @BeforeEach
    void setUp() {
        testContractGalaxy = ContractDto.builder()
                .id(uuid1)
                .contractName("Galaxy Cat")
                .contractStyle("PALE_ALE")
                .build();

        testContractJava = ContractDto.builder()
                .contractName("Java Jill")
                .contractStyle("PORTER")
                .build();

        testContractMangoBob = ContractDto.builder()
                .contractName("Mango Bobs")
                .contractStyle("IPA")
                .build();

//        testInventoryGalaxy = contractInventoryRepository.save(ContractInventory.builder()
//                .contract_service(testContractGalaxy)
//                .quantityOnHand(1000)
//                .build());
//
//        testInventoryJava = contractInventoryRepository.save(ContractInventory.builder()
//                .contract_service(testContractJava)
//                .quantityOnHand(10)
//                .build());

        testCustomer = customerRepository.save(Customer
                .builder()
                .customerName("Test 1").apiKey(Long.randomLong())
                .build());

        Set<ContractOrderLine> orderLines1 = new HashSet<>();
        orderLines1.add(ContractOrderLine.builder().contractId(testContractGalaxy.getId())
                .orderQuantity(15).quantityAllocated(0).build());
        orderLines1.add(ContractOrderLine.builder().contractId(testContractJava.getId())
                .orderQuantity(7).quantityAllocated(0).build());

        testOrder1 = contractOrderRepository.save(ContractOrder.builder()
                .orderStatus(OrderStatusEnum.NEW)
                .customer(testCustomer)
                .customerRef("testOrder1")
                .orderStatusCallbackUrl("http://example.com/post")
                .contractOrderLines(orderLines1)
                .build());

        orderLines1.forEach(line -> {
            line.setContractOrder(testOrder1);
        });

        contractOrderRepository.save(testOrder1);

        Set<ContractOrderLine> orderLines2 = new HashSet<>();
        orderLines2.add(ContractOrderLine.builder().contractId(testContractGalaxy.getId())
                .orderQuantity(15).quantityAllocated(0).build());
        orderLines2.add(ContractOrderLine.builder().contractId(testContractJava.getId())
                .orderQuantity(7).quantityAllocated(0).build());

        testOrder2 = contractOrderRepository.save(ContractOrder.builder()
                .orderStatus(OrderStatusEnum.NEW)
                .customer(testCustomer)
                .customerRef("testOrder2")
                .orderStatusCallbackUrl("http://example.com/post")
                .contractOrderLines(orderLines2)
                .build());

        orderLines2.forEach(line -> {
            line.setContractOrder(testOrder2);
        });

        contractOrderRepository.save(testOrder2);

        Set<ContractOrderLine> orderLines3 = new HashSet<>();
        orderLines3.add(ContractOrderLine.builder().contractId(testContractGalaxy.getId())
                .orderQuantity(15).quantityAllocated(0).build());
        orderLines3.add(ContractOrderLine.builder().contractId(testContractJava.getId())
                .orderQuantity(7).quantityAllocated(0).build());

        testOrder3 = contractOrderRepository.save(ContractOrder.builder()
                .orderStatus(OrderStatusEnum.NEW)
                .customer(testCustomer)
                .customerRef("testOrder3")
                .orderStatusCallbackUrl("http://example.com/post")
                .contractOrderLines(orderLines3)
                .build());

        orderLines3.forEach(line -> {
            line.setContractOrder(testOrder3);
        });
        contractOrderRepository.saveAndFlush(testOrder3);
    }
}
