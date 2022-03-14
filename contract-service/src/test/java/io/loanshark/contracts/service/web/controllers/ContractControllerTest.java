package io.loanshark.contracts.service.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.loanshark.contracts.service.bootstrap.DefaultContractsLoader;
import io.loanshark.contracts.service.services.ContractService;
import io.loanshark.contracts.service.web.model.ContractDto;
import io.loanshark.contracts.service.web.model.ContractPagedList;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.atlassian.oai.validator.mockmvc.OpenApiValidationMatchers.openApi;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.reset;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ContractController.class)
class ContractControllerTest {

    public static final String GALAXY_CAT = "Galaxy Cat";
    public static final String OAC_SPEC = "https://raw.githubusercontent.com/sfg-contract-works/brewery-api/master/spec/openapi.yaml";

    @MockBean
    ContractService contractService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Captor
    ArgumentCaptor<Long> uuidArgumentCaptor;

    ContractDto validContract;

    @BeforeEach
    void setUp() {
        validContract = ContractDto.builder()
/*
                .price(new BigDecimal("12.99"))
                .quantityOnHand(4)
                .upc(DefaultContractsLoader.BEER_1_UPC)*/
                .build();
    }

    @AfterEach
    void tearDown() {
        reset(contractService);
    }

    @DisplayName("List Ops - ")
    @Nested
    public class TestListOperations {

        @Captor
        ArgumentCaptor<String> contractNameCaptor;


        @Captor
        ArgumentCaptor<PageRequest> pageRequestCaptor;

        ContractPagedList contractPagedList;

        @BeforeEach
        void setUp() {
            List<ContractDto> contracts = new ArrayList<>();
            contracts.add(validContract);
            contracts.add(ContractDto.builder().id(1L)
                    .version(1)
                    /*.contractName("Contract4")
                    .upc(DefaultContractsLoader.BEER_2_UPC)
                    .price(new BigDecimal("12.99"))
                    .quantityOnHand(66)*/
                    .createdDate(OffsetDateTime.now())
                    .lastModifiedDate(OffsetDateTime.now())
                    .build());

            contractPagedList = new ContractPagedList(contracts, PageRequest.of(1, 1), 2L);

            given(contractService.listContracts(contractNameCaptor.capture(),
                    pageRequestCaptor.capture())).willReturn(contractPagedList);
        }

        @DisplayName("Test No Params")
        @Test
        void testNoParams() throws Exception {
            mockMvc.perform(get("/api/v1/contract").accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.content", hasSize(2)))
                    .andExpect(jsonPath("$.content[0].id", is(validContract.getId().toString())))
                    .andExpect(openApi().isValid(OAC_SPEC));

            then(contractService).should().listContracts(isNull(), any(PageRequest.class));
            assertThat(0).isEqualTo(pageRequestCaptor.getValue().getPageNumber());
            assertThat(25).isEqualTo(pageRequestCaptor.getValue().getPageSize());
        }

        @DisplayName("Test Page Size Param")
        @Test
        void testPageSizeParam() throws Exception {
            mockMvc.perform(get("/api/v1/contract").accept(MediaType.APPLICATION_JSON)
                    .param("pageSize", "200"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.content", hasSize(2)))
                    .andExpect(openApi().isValid(OAC_SPEC));

            then(contractService).should().listContracts(isNull(),  any(PageRequest.class));
            assertThat(0).isEqualTo(pageRequestCaptor.getValue().getPageNumber());
            assertThat(200).isEqualTo(pageRequestCaptor.getValue().getPageSize());
        }

        @DisplayName("Test Page Param")
        @Test
        void testPageParam() throws Exception {
            mockMvc.perform(get("/api/v1/contract").accept(MediaType.APPLICATION_JSON)
                    .param("pageSize", "200"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.content", hasSize(2)))
                    .andExpect(openApi().isValid(OAC_SPEC));

            then(contractService).should().listContracts(isNull(),  any(PageRequest.class));
            assertThat(0).isEqualTo(pageRequestCaptor.getValue().getPageNumber());
            assertThat(200).isEqualTo(pageRequestCaptor.getValue().getPageSize());
        }

        @DisplayName("Test Contract Name Param")
        @Test
        void testContractNameParam() throws Exception {
            mockMvc.perform(get("/api/v1/contract").accept(MediaType.APPLICATION_JSON)
                    .param("contractName", GALAXY_CAT))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.content", hasSize(2)))
                    .andExpect(openApi().isValid(OAC_SPEC));

            then(contractService).should().listContracts(anyString(),  any(PageRequest.class));
            assertThat(0).isEqualTo(pageRequestCaptor.getValue().getPageNumber());
            assertThat(25).isEqualTo(pageRequestCaptor.getValue().getPageSize());
            assertThat(GALAXY_CAT).isEqualToIgnoringCase(contractNameCaptor.getValue());
        }

        @DisplayName("Test Contract Style Param")
        @Test
        void testContractStyle() throws Exception {
            mockMvc.perform(get("/api/v1/contract").accept(MediaType.APPLICATION_JSON)
                    .param("contractStyle", "IPA"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.content", hasSize(2)))
                    .andExpect(openApi().isValid(OAC_SPEC));

            assertThat(0).isEqualTo(pageRequestCaptor.getValue().getPageNumber());
            assertThat(25).isEqualTo(pageRequestCaptor.getValue().getPageSize());
        }
    }

    @Test
    void getContractById() throws Exception {
        //given
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ");
        given(contractService.findContractById(any(Long.class))).willReturn(validContract);

        mockMvc.perform(get("/api/v1/contract/" + validContract.getId().toString()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(validContract.getId().toString())))
                .andExpect(jsonPath("$.contractName", is("Contract1")))
                .andExpect(jsonPath("$.createdDate", is(dateTimeFormatter.format(validContract.getCreatedDate()))))
                .andExpect(openApi().isValid(OAC_SPEC));

    }

    @DisplayName("Save Ops - ")
    @Nested
    public class TestSaveOperations {
        @Test
        void testSaveNewContract() throws Exception {
            //given
            ContractDto contractDto = validContract;
            contractDto.setId(null);
            ContractDto savedDto = ContractDto.builder().id(1L).build();
            String contractDtoJson = objectMapper.writeValueAsString(contractDto);

            given(contractService.saveContract(any())).willReturn(savedDto);

            mockMvc.perform(post("/api/v1/contract/")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(contractDtoJson))
                    .andExpect(status().isCreated());
        }

        @Test
        void testSaveNewContractBadRequest() throws Exception {
            //given
            ContractDto contractDto = validContract;
            contractDto.setId(null);
            contractDto.setRiskRating("A");
            ContractDto savedDto = ContractDto.builder().id(1L).build();
            String contractDtoJson = objectMapper.writeValueAsString(contractDto);

            given(contractService.saveContract(any())).willReturn(savedDto);

            mockMvc.perform(post("/api/v1/contract/")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(contractDtoJson))
                    .andExpect(status().isBadRequest());

            then(contractService).shouldHaveNoInteractions();
        }
    }

    @DisplayName("Save Ops - ")
    @Nested
    public class TestUpdateOperations {

        @Test
        void testUpdateContract() throws Exception {
            //given
            ContractDto contractDto = validContract;
            String contractDtoJson = objectMapper.writeValueAsString(contractDto);

            //when
            mockMvc.perform(put("/api/v1/contract/" + 1)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(contractDtoJson))
                    .andExpect(status().isNoContent());

            then(contractService).should().updateContract(any(), any());
        }

        @Test
        void testUpdateContractBadRequest() throws Exception {
            //given
            ContractDto contractDto = validContract;
            contractDto.setRiskRating(null);
            String contractDtoJson = objectMapper.writeValueAsString(contractDto);

            //when
            mockMvc.perform(put("/api/v1/contract/" + validContract.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(contractDtoJson))
                    .andExpect(status().isBadRequest());

            then(contractService).shouldHaveNoInteractions();
        }
    }
}