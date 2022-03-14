package io.loanshark.contracts.service.web.controllers;

import io.loanshark.contracts.service.web.model.ContractDto;
import io.loanshark.contracts.service.web.model.ContractPagedList;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import static org.assertj.core.api.Java6Assertions.assertThat;

/**
 * Created by jt on 2019-03-03.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ContractControllerIT {

    public static final String API_V_1_BEER = "/api/v1/contract/";

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testGetContracts() {
        ContractPagedList pagedList = restTemplate.getForObject(API_V_1_BEER, ContractPagedList.class);

        assertThat(pagedList.getContent()).hasSize(3);

        pagedList.getContent().forEach(contractDto -> {
            ContractDto fetchedContractDto = restTemplate.getForObject(API_V_1_BEER + contractDto.getId().toString(), ContractDto.class);

            assertThat(contractDto.getId()).isEqualByComparingTo(fetchedContractDto.getId());
        });
    }
}
