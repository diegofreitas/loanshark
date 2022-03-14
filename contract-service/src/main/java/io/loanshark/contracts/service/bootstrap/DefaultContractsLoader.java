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
package io.loanshark.contracts.service.bootstrap;

import io.loanshark.contracts.service.repositories.ContractRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


/**
 * Created by jt on 2019-01-26.
 */
@Component
public class DefaultContractsLoader implements CommandLineRunner {

    public static final String BEER_1_UPC = "0631234200036";
    public static final String BEER_2_UPC = "0631234300019";
    public static final String BEER_3_UPC = "0083783375213";

    private final ContractRepository contractRepository;


    public DefaultContractsLoader(ContractRepository contractRepository) {
        this.contractRepository = contractRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        loadBreweryData();
    }

    private void loadBreweryData() {
       /* if (breweryRepository.count() == 0){
            breweryRepository.save(Brewery
                    .builder()
                    .breweryName("Cage Brewing")
                    .build());

            Contract mangoBobs = Contract.builder()
                    .contractName("Mango Bobs")
                    .contractStyle(ContractStyleEnum.IPA)
                    .minOnHand(12)
                    .quantityToBrew(200)
                    .quantityOnHand(500)
                    .upc(BEER_1_UPC)
                    .build();

            contractRepository.save(mangoBobs);

            Contract galaxyCat = Contract.builder()
                    .contractName("Galaxy Cat")
                    .contractStyle(ContractStyleEnum.PALE_ALE)
                    .minOnHand(12)
                    .quantityToBrew(200)
                    .upc(BEER_2_UPC)
                    .build();

            contractRepository.save(galaxyCat);

            Contract pinball = Contract.builder()
                    .contractName("Pinball Porter")
                    .contractStyle(ContractStyleEnum.PORTER)
                    .minOnHand(12)
                    .quantityToBrew(200)
                    .upc(BEER_3_UPC)
                    .build();

            contractRepository.save(pinball);

        }*/
    }
}
