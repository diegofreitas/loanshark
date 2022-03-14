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

import com.github.jenspiegsa.wiremockextension.Managed;
import com.github.jenspiegsa.wiremockextension.WireMockExtension;
import com.github.tomakehurst.wiremock.WireMockServer;
import guru.sfg.brewery.order.service.domain.ContractOrder;
import guru.sfg.brewery.order.service.domain.OrderStatusEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.web.client.RestTemplateBuilder;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Long;

import static com.github.jenspiegsa.wiremockextension.ManagedWireMockServer.with;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

@ExtendWith(WireMockExtension.class)
class ContractOrderStatusChangeEventListenerTest {

    @Managed
    WireMockServer wireMockServer = with(wireMockConfig().dynamicPort());

    ContractOrderStatusChangeEventListener listener;

    @BeforeEach
    void setUp() {
        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
        listener = new ContractOrderStatusChangeEventListener(restTemplateBuilder);
    }

    @Test
    void listen() {
        wireMockServer.stubFor(post("/update").withRequestBody(matchingJsonPath("$.orderId")).willReturn(ok()));

        ContractOrder contractOrder = ContractOrder.builder().id(Long.randomLong())
                                    .orderStatus(OrderStatusEnum.READY)
                                    .orderStatusCallbackUrl("http://localhost:" + wireMockServer.port() + "/update")
                                    .createdDate(Timestamp.valueOf(LocalDateTime.now()))
                                    .build();

        ContractOrderStatusChangeEvent event = new ContractOrderStatusChangeEvent(contractOrder, OrderStatusEnum.NEW);

        listener.listen(event);

        verify(1, postRequestedFor(urlEqualTo("/update")));
    }
}