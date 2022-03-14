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

package io.loanshark.contracts.service.web.controllers;

import io.loanshark.contracts.service.services.ContractService;
import io.loanshark.contracts.service.web.model.ContractDto;
import io.loanshark.contracts.service.web.model.ContractPagedList;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RequestMapping("/api/v1/contracts")
@RestController
public class ContractController {

    private static final Integer DEFAULT_PAGE_NUMBER = 0;
    private static final Integer DEFAULT_PAGE_SIZE = 25;

    private final ContractService contractService;

    public ContractController(ContractService contractService) {
        this.contractService = contractService;
    }

    @GetMapping(produces = { "application/json" })
    public ResponseEntity<ContractPagedList> listContracts(@RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                                       @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                       @RequestParam(value = "contractName", required = false) String contractName){

        if (pageNumber == null || pageNumber < 0){
            pageNumber = DEFAULT_PAGE_NUMBER;
        }

        if (pageSize == null || pageSize < 1) {
            pageSize = DEFAULT_PAGE_SIZE;
        }

        ContractPagedList contractList = contractService.listContracts(contractName, PageRequest.of(pageNumber, pageSize));

        return new ResponseEntity<>(contractList, HttpStatus.OK);
    }

    @GetMapping(path = {"/{contractId}"}, produces = { "application/json" })
    public ResponseEntity<ContractDto>  getContractById(@PathVariable("contractId") Long contractId){

        return new ResponseEntity<>(contractService.findContractById(contractId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity saveNewContract(@Valid @RequestBody ContractDto contractDto){

        ContractDto savedDto = contractService.saveContract(contractDto);

        HttpHeaders httpHeaders = new HttpHeaders();

        //todo hostname for uri
        httpHeaders.add("Location", "/api/v1/contract_service/" + savedDto.getId().toString());

        return new ResponseEntity(httpHeaders, HttpStatus.CREATED);
    }

    @PutMapping(path = {"/{contractId}"}, produces = { "application/json" })
    public ResponseEntity updateContract(@PathVariable("contractId") Long contractId, @Valid @RequestBody ContractDto contractDto){

        contractService.updateContract(contractId, contractDto);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping({"/{contractId}"})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteContract(@PathVariable("contractId") Long contractId){
        contractService.deleteById(contractId);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<List> badReqeustHandler(ConstraintViolationException e){
        List<String> errors = new ArrayList<>(e.getConstraintViolations().size());

        e.getConstraintViolations().forEach(constraintViolation -> {
            errors.add(constraintViolation.getPropertyPath().toString() + " : " + constraintViolation.getMessage());
        });

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

}
