package io.loanshark.contracts.service.services;

import io.loanshark.contracts.service.domain.Contract;
import io.loanshark.contracts.service.repositories.ContractRepository;
import io.loanshark.contracts.service.web.mappers.ContractMapper;
import io.loanshark.contracts.service.web.mappers.ContractMapperImpl;
import io.loanshark.contracts.service.web.mappers.DateMapper;
import io.loanshark.contracts.service.web.model.ContractDto;
import io.loanshark.contracts.service.web.model.ContractPagedList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@SpringJUnitConfig(classes = {ContractServiceImplTest.ContractServiceConfig.class})
class ContractServiceImplTest {

    @Configuration
    static class ContractServiceConfig {

        @Bean
        DateMapper dateMapper() {
            return new DateMapper();
        }

        @Bean
        ContractMapper contractMapper() {
            return new ContractMapperImpl();
        }

        @Bean
        ContractServiceImpl contractService(ContractRepository contractRepository, ContractMapper mapper) {
            return new ContractServiceImpl(contractRepository, mapper);
        }
    }

    @MockBean
    ContractRepository contractRepository;

    @Autowired
    ContractServiceImpl contractService;

    @DisplayName("List Ops - ")
    @Nested
    public class TestListOptions {
        private List<Contract> contractList;
        private PageImpl<Contract> contractPage;

        @BeforeEach
        void setUp() {
            this.contractList = new ArrayList<>();
            List<Contract> contractList = new ArrayList<>();
            contractList.add(Contract.builder().id((long) (Math.random()*1000000)).build());
            contractList.add(Contract.builder().id((long) (Math.random()*1000000)).build());
            contractPage = new PageImpl<>(contractList, PageRequest.of(1, 25), 2);
        }

        @DisplayName("Test Find By Name and Style")
        @Test
        void listContractsTestFindByNameAndStyle() {
            //given
            given(contractRepository.findAll(
                    any(PageRequest.class))).willReturn(contractPage);

            //when
            ContractPagedList contractPagedList = contractService.listContracts("uuum IPA contract_service",
                    PageRequest.of(1, 25));

            //then
            assertThat(2).isEqualTo(contractPagedList.getContent().size());
        }

        @DisplayName("Test Find By Name Only")
        @Test
        void listContractsTestFindByNameOnly() {
            //given
            given(contractRepository.findAll( any(PageRequest.class))).willReturn(contractPage);

            //when
            ContractPagedList contractPagedList = contractService.listContracts("uuum IPA contract_service",
                    PageRequest.of(1, 25));

            //then
            assertThat(2).isEqualTo(contractPagedList.getContent().size());
        }

        @DisplayName("Test Find By Style Only")
        @Test
        void listContractsTestFindByStyleOnly() {
            //given
            given(contractRepository.findAll( any(PageRequest.class))).willReturn(contractPage);

            //when
            ContractPagedList contractPagedList = contractService.listContracts(null,
                    PageRequest.of(1, 25));

            //then
            assertThat(2).isEqualTo(contractPagedList.getContent().size());
        }

        @DisplayName("Test Find All")
        @Test
        void listContractsTestFindAll() {
            //given
            given(contractRepository.findAll(any(PageRequest.class))).willReturn(contractPage);

            //when
            ContractPagedList contractPagedList = contractService.listContracts(null,
                    PageRequest.of(1, 25));

            //then
            assertThat(2).isEqualTo(contractPagedList.getContent().size());
        }
    }

    @DisplayName("Find By Long")
    @Test
    void findContractById() {
        //given
        given(contractRepository.findById(any(Long.class))).willReturn(Optional.of(Contract.builder().build()));
        //when
        ContractDto contractDto = contractService.findContractById((long) (Math.random()*1000000));

        //then
        assertThat(contractDto).isNotNull();
    }

    @DisplayName("Find By Long Not Found")
    @Test
    void findContractByIdNotFound() {
        //given
        given(contractRepository.findById(any(Long.class))).willReturn(Optional.empty());

        //when/then
        assertThrows(RuntimeException.class, () -> contractService.findContractById((long) (Math.random()*1000000)));
    }

    @Test
    void testSaveContract() {
        Contract savedContract = Contract.builder().id((long) (Math.random()*1000000)).build();
        ContractDto newContract = ContractDto.builder().riskRating("A").build();
        given(contractRepository.save(any())).willReturn(savedContract);

        ContractDto savedContractDto = contractService.saveContract(newContract);

        then(contractRepository).should().save(any());

        assertEquals(savedContract.getId(), savedContractDto.getId());
    }

    @Test
    void testUpdateContract() {
        Contract updated = Contract.builder().id((long) (Math.random()*1000000)).build();
        ContractDto contractDto = ContractDto.builder().build();

        given(contractRepository.save(any())).willReturn(updated);
        given(contractRepository.findById(any())).willReturn(Optional.of(updated));

        contractService.updateContract(updated.getId(), contractDto);

        then(contractRepository).should().save(any());
    }
}