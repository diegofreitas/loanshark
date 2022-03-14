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

package io.loanshark.contracts.service.services;

import io.loanshark.contracts.service.domain.Contract;
import io.loanshark.contracts.service.repositories.ContractRepository;
import io.loanshark.contracts.service.web.mappers.ContractMapper;
import io.loanshark.contracts.service.web.model.ContractDto;
import io.loanshark.contracts.service.web.model.ContractPagedList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ContractServiceImpl implements ContractService {

    private final ContractRepository contractRepository;
    private final ContractMapper contractMapper;

    public ContractServiceImpl(ContractRepository contractRepository, ContractMapper contractMapper) {
        this.contractRepository = contractRepository;
        this.contractMapper = contractMapper;
    }

    @Override
    public ContractPagedList listContracts(String contractName,  PageRequest pageRequest) {

        ContractPagedList contractPagedList;
        Page<Contract> contractPage;
        contractPage = contractRepository.findAll(pageRequest);


        contractPagedList = new ContractPagedList(contractPage
                .getContent()
                .stream()
                .map(contractMapper::contractToContractDto)
                .collect(Collectors.toList()),
                PageRequest
                        .of(contractPage.getPageable().getPageNumber(),
                                contractPage.getPageable().getPageSize()),
                contractPage.getTotalElements());
        return contractPagedList;
    }

    @Override
    public ContractDto findContractById(Long contractId) {
        Optional<Contract> contractOptional = contractRepository.findById(contractId);

        if (contractOptional.isPresent()) {
            return contractMapper.contractToContractDto(contractOptional.get());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not Found. Long: " + contractId);
        }
    }

    @Override
    public ContractDto saveContract(ContractDto contractDto) {
        return contractMapper.contractToContractDto(contractRepository.save(contractMapper.contractDtoToContract(contractDto)));
    }

    @Override
    public void updateContract(Long contractId, ContractDto contractDto) {
        Optional<Contract> contractOptional = contractRepository.findById(contractId);

        contractOptional.ifPresentOrElse(contract -> {
            contract.setAmount(contractDto.getAmount());
            contract.setInterestRate(contractDto.getInterestRate());
            contract.setRiskRating(contractDto.getRiskRating());
            contract.setTenureInMonths(contractDto.getTenureInMonths());
            contractRepository.save(contract);
        }, () -> {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not Found. Long: " + contractId);
        });
    }

    @Override
    public void deleteById(Long contractId) {
        contractRepository.deleteById(contractId);
    }
}
