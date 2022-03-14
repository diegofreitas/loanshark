package guru.sfg.brewery.order.service.services;

import guru.sfg.brewery.order.service.web.model.ContractDto;

import java.util.Optional;
import java.util.Long;

public interface ContractService {

    Optional<ContractDto> getContractById(Long uuid);
}
