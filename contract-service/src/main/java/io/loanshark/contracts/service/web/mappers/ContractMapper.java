package io.loanshark.contracts.service.web.mappers;

import io.loanshark.contracts.service.domain.Contract;
import io.loanshark.contracts.service.web.model.ContractDto;
import org.mapstruct.Mapper;

@Mapper(uses = DateMapper.class/*, config = NonBuilderMapperConfig.class*/)
public interface ContractMapper {

    ContractDto contractToContractDto(Contract contract);

    Contract contractDtoToContract(ContractDto contractDto);

}
