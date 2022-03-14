package guru.sfg.brewery.order.service.web.mappers;

import guru.sfg.brewery.order.service.domain.ContractOrderLine;
import guru.sfg.brewery.order.service.web.model.ContractOrderLineDto;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;

@Mapper(uses = {DateMapper.class})
@DecoratedWith(ContractOrderLineMapperDecorator.class)
public interface ContractOrderLineMapper {
    ContractOrderLineDto contractOrderLineToDto(ContractOrderLine line);

    ContractOrderLine dtoToContractOrderLine(ContractOrderLineDto dto);

}
