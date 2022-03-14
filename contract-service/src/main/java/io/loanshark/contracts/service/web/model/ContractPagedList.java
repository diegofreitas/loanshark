
package io.loanshark.contracts.service.web.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class ContractPagedList extends PageImpl<ContractDto> {
    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public ContractPagedList(@JsonProperty("content") List<ContractDto> content,
                             @JsonProperty("number") int number,
                             @JsonProperty("size") int size,
                             @JsonProperty("totalElements") Long totalElements,
                             @JsonProperty("pageable") JsonNode pageable,
                             @JsonProperty("last") boolean last,
                             @JsonProperty("totalPages") int totalPages,
                             @JsonProperty("sort") JsonNode sort,
                             @JsonProperty("first") boolean first,
                             @JsonProperty("numberOfElements") int numberOfElements) {

        super(content, PageRequest.of(number, size), totalElements);
    }

    public ContractPagedList(List<ContractDto> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }

    public ContractPagedList(List<ContractDto> content) {
        super(content);
    }
}
