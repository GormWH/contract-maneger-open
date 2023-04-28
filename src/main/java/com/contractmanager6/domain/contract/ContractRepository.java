package com.contractmanager6.domain.contract;

import java.util.List;
import java.util.Optional;

public interface ContractRepository {
    Contract save(Contract contract);
    void deleteById(Long contractId);
    Optional<Contract> findById(Long id);
    List<Contract> findByCompany(String company);
    List<Contract> findByContractor(String contractor);
    List<Contract> findAll();
    List<Contract> findByCompanyContains(String keyword);
    List<Contract> findByCompanyExact(String keyword);
}
