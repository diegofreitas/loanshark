package guru.sfg.brewery.order.service.web.mappers;

import guru.sfg.brewery.order.service.domain.ContractOrderLine;
import guru.sfg.brewery.order.service.services.ContractService;
import guru.sfg.brewery.order.service.web.model.ContractDto;
import guru.sfg.brewery.order.service.web.model.ContractOrderLineDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Optional;

public abstract class ContractOrderLineMapperDecorator implements ContractOrderLineMapper {

    private ContractService contractService;
    private ContractOrderLineMapper contractOrderLineMapper;

    @Autowired
    public void setContractService(ContractService contractService) {
        this.contractService = contractService;
    }

    @Autowired
    @Qualifier("delegate")
    public void setContractOrderLineMapper(ContractOrderLineMapper contractOrderLineMapper) {
        this.contractOrderLineMapper = contractOrderLineMapper;
    }

    @Override
    public ContractOrderLineDto contractOrderLineToDto(ContractOrderLine line) {
        ContractOrderLineDto orderLineDto = contractOrderLineMapper.contractOrderLineToDto(line);
        Optional<ContractDto> contractDtoOptional = contractService.getContractById(line.getContractId());

        contractDtoOptional.ifPresent(contractDto -> {
            orderLineDto.setContractName(contractDto.getContractName());
            orderLineDto.setContractStyle(contractDto.getContractName());
            orderLineDto.setUpc(contractDto.getUpc());
            orderLineDto.setPrice(contractDto.getPrice());
        });

        return orderLineDto;
    }
}
