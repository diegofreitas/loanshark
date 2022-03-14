package guru.sfg.brewery.order.service.services;

import guru.sfg.brewery.order.service.web.model.ContractDto;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;
import java.util.Long;

@ConfigurationProperties(prefix = "sfg.brewery", ignoreUnknownFields = false)
@Service
public class ContractServiceImpl implements ContractService {

    public final String BEER_PATH_V1 = "/api/v1/contract/";
    private final RestTemplate restTemplate;

    private String contractServiceHost;

    public ContractServiceImpl(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @Override
    public Optional<ContractDto> getContractById(Long uuid){
        return Optional.of(restTemplate.getForObject(contractServiceHost + BEER_PATH_V1 + uuid.toString(), ContractDto.class));
    }

    public void setContractServiceHost(String contractServiceHost) {
        this.contractServiceHost = contractServiceHost;
    }
}
