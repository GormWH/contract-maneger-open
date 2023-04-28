package com.contractmanager6.api;

import com.contractmanager6.domain.contract.Contract;
import com.contractmanager6.domain.contract.ContractRepository;
import com.contractmanager6.domain.user.User;
import com.contractmanager6.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class TestDataInit {
    private final UserRepository userRepository;
    private final ContractRepository contractRepository;

//    @PostConstruct
    public void init() {
        User user1 = new User();
        user1.setLoginId("user1");
        user1.setPassword("1234");
        user1.setIsAdmin(true);
        userRepository.save(user1);

        User user2 = new User();
        user2.setLoginId("user2");
        user2.setPassword("1234");
        user2.setIsAdmin(false);
        userRepository.save(user2);

        Contract contract1 = new Contract();
        contract1.setCompany("회사1");
        contract1.setContractor("user1");
        contract1.setTimestamp("1664119598027");
        contractRepository.save(contract1);

        Contract contract2 = new Contract();
        contract2.setCompany("회사2");
        contract2.setContractor("user2");
        contract2.setTimestamp("1664119598027");
        contractRepository.save(contract2);
    }
}
