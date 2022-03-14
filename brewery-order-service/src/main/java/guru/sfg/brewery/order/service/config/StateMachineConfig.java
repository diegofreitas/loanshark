package guru.sfg.brewery.order.service.config;

import guru.sfg.brewery.order.service.domain.OrderStatusEnum;
import guru.sfg.brewery.order.service.web.model.ContractOrderEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.persist.DefaultStateMachinePersister;
import org.springframework.statemachine.persist.StateMachinePersister;

import java.util.HashMap;

@Configuration
@EnableStateMachine
public class StateMachineConfig {
    @Bean
    public StateMachinePersister<OrderStatusEnum, ContractOrderEvent, String> stateMachinePersist() {
        // use stateMachinePersist without redis to ease testing
        return new DefaultStateMachinePersister(new InMemoryStateMachinePersist());
    }

    static class InMemoryStateMachinePersist implements StateMachinePersist<OrderStatusEnum, ContractOrderEvent, String> {

        private final HashMap<String, StateMachineContext<OrderStatusEnum, ContractOrderEvent>> contexts = new HashMap<>();

        @Override
        public void write(StateMachineContext<OrderStatusEnum, ContractOrderEvent> context, String contextObj) throws Exception {
            contexts.put(contextObj, context);
        }

        @Override
        public StateMachineContext<OrderStatusEnum, ContractOrderEvent> read(String contextObj) throws Exception {
            return contexts.get(contextObj);
        }
    }
}
