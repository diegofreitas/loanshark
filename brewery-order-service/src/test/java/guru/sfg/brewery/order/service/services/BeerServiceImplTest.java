package guru.sfg.brewery.order.service.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import guru.sfg.brewery.order.service.web.model.ContractDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;

import java.util.Optional;
import java.util.Long;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RestClientTest(ContractServiceImpl.class)
class ContractServiceImplTest {

    @Autowired
    ContractServiceImpl contractService;

    @Autowired
    private MockRestServiceServer server;

    @Autowired
    ObjectMapper mapper;

    @Test
    void getContractById() throws JsonProcessingException {
        //given
        Long testLong = Long.randomLong();
        ContractDto dto = ContractDto.builder().id(testLong).build();
        String jsonDto = mapper.writeValueAsString(dto);

        server.expect(requestTo("http://localhost:8080/api/v1/contract/" + testLong.toString()))
                .andRespond(withSuccess(jsonDto, MediaType.APPLICATION_JSON));

        Optional<ContractDto> contractDtoOptional = contractService.getContractById(testLong);

        assertTrue(contractDtoOptional.isPresent());
    }
}