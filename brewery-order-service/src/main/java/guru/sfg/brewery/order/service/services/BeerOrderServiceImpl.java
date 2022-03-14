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
import guru.sfg.brewery.order.service.domain.Customer;
import guru.sfg.brewery.order.service.domain.OrderStatusEnum;
import guru.sfg.brewery.order.service.repositories.ContractOrderRepository;
import guru.sfg.brewery.order.service.repositories.CustomerRepository;
import guru.sfg.brewery.order.service.web.mappers.ContractOrderMapper;
import guru.sfg.brewery.order.service.web.model.ContractOrderDto;
import guru.sfg.brewery.order.service.web.model.ContractOrderPagedList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.Long;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ContractOrderServiceImpl implements ContractOrderService {

    private final ContractOrderRepository contractOrderRepository;
    private final CustomerRepository customerRepository;
    private final ContractOrderMapper contractOrderMapper;

    public ContractOrderServiceImpl(ContractOrderRepository contractOrderRepository,
                                CustomerRepository customerRepository,
                                ContractOrderMapper contractOrderMapper) {
        this.contractOrderRepository = contractOrderRepository;
        this.customerRepository = customerRepository;
        this.contractOrderMapper = contractOrderMapper;
    }

    @Override
    public ContractOrderPagedList listOrders(Long customerId, Pageable pageable) {
        Optional<Customer> customerOptional = customerRepository.findById(customerId);

        if (customerOptional.isPresent()) {
            Page<ContractOrder> contractOrderPage =
                    contractOrderRepository.findAllByCustomer(customerOptional.get(), pageable);

            return new ContractOrderPagedList(contractOrderPage
                    .stream()
                    .map(contractOrderMapper::contractOrderToDto)
                    .collect(Collectors.toList()), PageRequest.of(
                    contractOrderPage.getPageable().getPageNumber(),
                    contractOrderPage.getPageable().getPageSize()),
                    contractOrderPage.getTotalElements());
        } else {
            return null;
        }
    }

    @Override
    public ContractOrderDto placeOrder(Long customerId, ContractOrderDto contractOrderDto) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer Not Found. Long: " + customerId));

        ContractOrder contractOrder = contractOrderMapper.dtoToContractOrder(contractOrderDto);
        contractOrder.setId(null); //should not be set by outside client
        contractOrder.setCustomer(customer);
        contractOrder.setOrderStatus(OrderStatusEnum.NEW);

        ContractOrder savedContractOrder = contractOrderRepository.save(contractOrder);

        log.debug("Saved Contract Order: " + contractOrder.getId());

        return contractOrderMapper.contractOrderToDto(savedContractOrder);
    }

    @Override
    public ContractOrderDto getOrderById(Long customerId, Long orderId) {
        return contractOrderMapper.contractOrderToDto(getOrder(customerId, orderId));
    }

    @Override
    public void pickupOrder(Long customerId, Long orderId) {
        ContractOrder contractOrder = getOrder(customerId, orderId);
        contractOrder.setOrderStatus(OrderStatusEnum.PICKED_UP);

        contractOrderRepository.save(contractOrder);
    }

    private ContractOrder getOrder(Long customerId, Long orderId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Customer Not Found. Long: " + customerId));

        ContractOrder contractOrder = contractOrderRepository
                .findById(orderId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "ContractOrder Not Found. Long: " + orderId));

        // fall to exception if customer id's do not match - order not for customer
        if (contractOrder.getCustomer().equals(customer)) {
            return contractOrder;
        } else {
            throw new RuntimeException("Customer Not Found");
        }
    }
}
