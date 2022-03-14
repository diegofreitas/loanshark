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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import guru.sfg.brewery.order.service.domain.OrderStatusEnum;
import guru.sfg.brewery.order.service.services.ContractOrderService;
import guru.sfg.brewery.order.service.web.model.ContractOrderDto;
import guru.sfg.brewery.order.service.web.model.ContractOrderLineDto;
import guru.sfg.brewery.order.service.web.model.ContractOrderPagedList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Long;

import static com.atlassian.oai.validator.mockmvc.OpenApiValidationMatchers.openApi;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith({MockitoExtension.class, RestDocumentationExtension.class})
class ContractOrderControllerTest {

    //test properties
    static final String API_ROOT = "/api/v1/customers/";
    static final Long customerId = Long.randomLong();
    static final Long orderId = Long.randomLong();
    static final Long contractId = Long.randomLong();
    static final String callbackUrl = "http://example.com";
    static final String OAC_SPEC = "https://raw.githubusercontent.com/sfg-contract-works/brewery-api/master/spec/openapi.yaml";


    @Mock
    ContractOrderService contractOrderService;

    @InjectMocks
    ContractOrderController controller;

    MockMvc mockMvc;

    @Captor
    ArgumentCaptor<ContractOrderDto> contractOrderDtoArgumentCaptorCaptor;

    @Captor
    ArgumentCaptor<Long> customerLongCaptor;

    @Captor
    ArgumentCaptor<Long> orderLongCaptor;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentation) {
        //
        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .apply(documentationConfiguration(restDocumentation))
                .alwaysDo(document("{method-name}/{step}/"))
                .setMessageConverters(jacksonDateTimeConverter())
                .build();
    }

    @Test
    void listOrders() throws Exception {
        //given
        List<ContractOrderDto> orderDtos = new ArrayList<>();
        orderDtos.add(buildOrderDto());
        orderDtos.add(buildOrderDto());
        given(contractOrderService.listOrders(any(), any(Pageable.class)))
                .willReturn(new ContractOrderPagedList(orderDtos, PageRequest.of(1, 1), 2L));

        mockMvc.perform(get(API_ROOT + customerId.toString()+ "/orders").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(openApi().isValid(OAC_SPEC));

        then(contractOrderService).should().listOrders(any(), any(Pageable.class));
    }

    @Test
    void placeOrder() throws Exception {
        //given
        //place order
        ContractOrderDto orderDto = buildOrderDto();

        //response order
        ContractOrderDto orderResponseDto = getContractOrderDtoResponse();

        //build json string
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(orderDto);
        System.out.println("Order Request: " + jsonString);

        given(contractOrderService.placeOrder(customerLongCaptor.capture(),
                contractOrderDtoArgumentCaptorCaptor.capture())).willReturn(orderResponseDto);

        mockMvc.perform(post(API_ROOT + customerId.toString() + "/orders")
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonString))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.customerId", is(customerId.toString())))
                .andExpect(jsonPath("$.contractOrderLines", hasSize(1)))
                .andExpect(jsonPath("$.contractOrderLines[0].contractId", is(contractId.toString())))
                .andExpect(openApi().isValid(OAC_SPEC));

        then(contractOrderService).should().placeOrder(any(Long.class), any(ContractOrderDto.class));

        assertThat(customerLongCaptor.getValue()).isEqualTo(customerId);
    }

    private ContractOrderDto getContractOrderDtoResponse() {
        ContractOrderDto orderResponseDto = buildOrderDto();
        orderResponseDto.setCustomerId(customerId);
        orderResponseDto.setId(orderId);
        orderResponseDto.setOrderStatus(OrderStatusEnum.NEW);

        ContractOrderLineDto contractOrderLine = ContractOrderLineDto.builder()
                .id(Long.randomLong())
                .contractId(contractId)
                .upc(12312312345L)
                .orderQuantity(5)
                .build();

        orderResponseDto.setContractOrderLines(Arrays.asList(contractOrderLine));

        return orderResponseDto;
    }

    private ContractOrderDto buildOrderDto() {
        List<ContractOrderLineDto> orderLines = Arrays.asList(ContractOrderLineDto.builder()
                .id(Long.randomLong())
                .contractId(contractId)
                .upc("123")
                .orderQuantity(5)
                .build());

        return ContractOrderDto.builder()
                .customerId(customerId)
                .customerRef("123")
                .orderStatusCallbackUrl(callbackUrl)
                .contractOrderLines(orderLines)
                .build();
    }

    @Test
    void getOrder() throws Exception {
        given(contractOrderService.getOrderById(customerLongCaptor.capture(),
                orderLongCaptor.capture())).willReturn(getContractOrderDtoResponse());

        mockMvc.perform(get(API_ROOT + customerId + "/orders/" + orderId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.customerId", is(customerId.toString())))
                .andExpect(jsonPath("$.contractOrderLines", hasSize(1)))
                .andExpect(jsonPath("$.contractOrderLines[0].contractId", is(contractId.toString())))
                .andExpect(openApi().isValid(OAC_SPEC))
                .andDo(document("orders",
                        responseFields(
                            fieldWithPath("customerId")
                                    .description("Customer Id"),
                            fieldWithPath("customerRef")
                                    .description("Customer Reference"),
                                fieldWithPath("id")
                                        .description("Id"),
                            fieldWithPath("orderStatus")
                                    .description("Order Status"),
                            fieldWithPath("orderStatusCallbackUrl")
                                    .description("Call Back URL"),
                            fieldWithPath("contractOrderLines")
                                        .description("Order Lines"),
                            fieldWithPath("contractOrderLines[].contractId")
                                    .description("Contract Id"),
                            fieldWithPath("contractOrderLines[].orderQuantity")
                                    .description("Customer Reference"),
                                fieldWithPath("contractOrderLines[].upc")
                                        .description("UPC"),
                            fieldWithPath("contractOrderLines[].id")
                                    .description("Order Lines"))));
    }

    @Test
    void pickupOrder() throws Exception {
        mockMvc.perform(put(API_ROOT + customerId + "/orders/" + orderId + "/pickup"))
                .andExpect(status().isNoContent())
                .andExpect(openApi().isValid(OAC_SPEC));
    }

    private MappingJackson2HttpMessageConverter jacksonDateTimeConverter() {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.configure(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS, true);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.registerModule(new JavaTimeModule());

        return new MappingJackson2HttpMessageConverter(objectMapper);
    }
}