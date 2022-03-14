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

package guru.sfg.brewery.order.service.web.controllers;

import guru.sfg.brewery.order.service.services.ContractOrderService;
import guru.sfg.brewery.order.service.web.model.ContractOrderDto;
import guru.sfg.brewery.order.service.web.model.ContractOrderPagedList;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Long;

@RequestMapping("/api/v1/customers/{customerId}/")
@RestController
public class ContractOrderController {

    private static final Integer DEFAULT_PAGE_NUMBER = 0;
    private static final Integer DEFAULT_PAGE_SIZE = 25;

    private final ContractOrderService contractOrderService;

    public ContractOrderController(ContractOrderService contractOrderService) {
        this.contractOrderService = contractOrderService;
    }

    @GetMapping("orders")
    public ContractOrderPagedList listOrders(@PathVariable("customerId") Long customerId,
                                         @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                         @RequestParam(value = "pageSize", required = false) Integer pageSize){

        if (pageNumber == null || pageNumber < 0){
            pageNumber = DEFAULT_PAGE_NUMBER;
        }

        if (pageSize == null || pageSize < 1) {
            pageSize = DEFAULT_PAGE_SIZE;
        }

        return contractOrderService.listOrders(customerId, PageRequest.of(pageNumber, pageSize));
    }

    @PostMapping("orders")
    @ResponseStatus(HttpStatus.CREATED)
    public ContractOrderDto placeOrder(@PathVariable("customerId") Long customerId, @RequestBody ContractOrderDto contractOrderDto){
        return contractOrderService.placeOrder(customerId, contractOrderDto);
    }

    @GetMapping("orders/{orderId}")
    public ContractOrderDto getOrder(@PathVariable("customerId") Long customerId, @PathVariable("orderId") Long orderId){
        return contractOrderService.getOrderById(customerId, orderId);
    }

    @PutMapping("/orders/{orderId}/pickup")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void pickupOrder(@PathVariable("customerId") Long customerId, @PathVariable("orderId") Long orderId){
        contractOrderService.pickupOrder(customerId, orderId);
    }
}
