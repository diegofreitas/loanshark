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

package guru.sfg.brewery.order.service.events;

import guru.sfg.brewery.order.service.web.mappers.DateMapper;
import guru.sfg.brewery.order.service.web.model.OrderStatusUpdate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class ContractOrderStatusChangeEventListener {

    RestTemplate restTemplate;
    DateMapper dateMapper = new DateMapper();

    public ContractOrderStatusChangeEventListener(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @Async
    @EventListener
    public void listen(ContractOrderStatusChangeEvent event){

        OrderStatusUpdate update = OrderStatusUpdate.builder()
                .id(event.getContractOrder().getId())
                .orderId(event.getContractOrder().getId())
                .version(event.getContractOrder().getVersion() != null ? event.getContractOrder().getVersion().intValue() : null)
                .createdDate(dateMapper.asOffsetDateTime(event.getContractOrder().getCreatedDate()))
                .lastModifiedDate(dateMapper.asOffsetDateTime(event.getContractOrder().getLastModifiedDate()))
                .orderStatus(event.getContractOrder().getOrderStatus().toString())
                .customerRef(event.getContractOrder().getCustomerRef())
                .build();

        try{
            log.debug("Posting to callback url");
            restTemplate.postForObject(event.getContractOrder().getOrderStatusCallbackUrl(), update, String.class);
        } catch (Throwable t){
            log.error("Error Preforming callback for order: " + event.getContractOrder().getId(), t);
        }
    }
}
