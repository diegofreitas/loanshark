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


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Service to allocate inventory to orders.
 *
 */
@Slf4j
@Service
public class ContractOrderAllocationService {

//    private final ContractOrderRepository contractOrderRepository;
//    private final ContractInventoryRepository contractInventoryRepository;
//
//    public ContractOrderAllocationService(ContractOrderRepository contractOrderRepository, ContractInventoryRepository contractInventoryRepository) {
//        this.contractOrderRepository = contractOrderRepository;
//        this.contractInventoryRepository = contractInventoryRepository;
//    }
//
//    @Transactional
//    @Scheduled(fixedRate = 5000) //run every 5 seconds
//    public void runContractOrderAllocation(){
//        log.debug("Starting Contract Order Allocation");
//
//        List<ContractOrder> newOrders = contractOrderRepository.findAllByOrderStatus(OrderStatusEnum.NEW);
//
//        if (newOrders.size() > 0 ) {
//
//            log.debug("Number of orders found to allocate: " + newOrders.size());
//
//            newOrders.forEach(contractOrder -> {
//                log.debug("Allocating Order" + contractOrder.getCustomerRef());
//
//                AtomicInteger totalOrdered = new AtomicInteger();
//                AtomicInteger totalAllocated = new AtomicInteger();
//
//                contractOrder.getContractOrderLines().forEach(contractOrderLine -> {
//                    if ((contractOrderLine.getOrderQuantity() - contractOrderLine.getQuantityAllocated()) > 0) {
//                        allocateContractOrderLine(contractOrderLine);
//                    }
//                    totalOrdered.set(totalOrdered.get() + contractOrderLine.getOrderQuantity());
//                    totalAllocated.set(totalAllocated.get() + contractOrderLine.getQuantityAllocated());
//                });
//
//                if(totalOrdered.get() == totalAllocated.get()){
//                    log.debug("Order Completely Allocated: " + contractOrder.getCustomerRef());
//                    contractOrder.setOrderStatus(OrderStatusEnum.READY);
//                }
//            });
//        } else {
//            log.debug("No Orders To Allocate");
//        }
//
//        //update orders
//        contractOrderRepository.saveAll(newOrders);
//
//    }
//
//    private void allocateContractOrderLine(ContractOrderLine contractOrderLine) {
//        List<ContractInventory> contractInventoryList = contractInventoryRepository.findAllByContract(contractOrderLine.getContract());
//
//        contractInventoryList.forEach(contractInventory -> {
//            int inventory = (contractInventory.getQuantityOnHand() == null) ? 0 : contractInventory.getQuantityOnHand();
//            int orderQty = (contractOrderLine.getOrderQuantity() == null) ? 0 : contractOrderLine.getOrderQuantity() ;
//            int allocatedQty = (contractOrderLine.getQuantityAllocated() == null) ? 0 : contractOrderLine.getQuantityAllocated();
//            int qtyToAllocate = orderQty - allocatedQty;
//
//            if(inventory >= qtyToAllocate){ // full allocation
//                inventory = inventory - qtyToAllocate;
//                contractOrderLine.setQuantityAllocated(orderQty);
//                contractInventory.setQuantityOnHand(inventory);
//            } else if (inventory > 0) { //partial allocation
//                contractOrderLine.setQuantityAllocated(allocatedQty + inventory);
//                contractInventory.setQuantityOnHand(0);
//            }
//        });
//
//        contractInventoryRepository.saveAll(contractInventoryList);
//
//        //remove zero records
//        List<ContractInventory> zeroRecs = new ArrayList<>();
//
//        contractInventoryList.stream()
//                .filter(contractInventory -> contractInventory.getQuantityOnHand() == 0)
//                .forEach(zeroRecs::add);
//
//        contractInventoryRepository.deleteAll(zeroRecs);
//    }
}
