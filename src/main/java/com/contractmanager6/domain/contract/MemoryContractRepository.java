package com.contractmanager6.domain.contract;

import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

//@Repository
public class MemoryContractRepository implements ContractRepository {

    private static final Map<Long, Contract> store = new HashMap<>();
    private static Long sequence = 0L;

    @Override
    public Contract save(Contract contract) {
        contract.setId(++sequence);
        store.put(contract.getId(), contract);
        return contract;
    }

    @Override
    public void deleteById(Long contractId) {
        store.remove(contractId);
    }

    @Override
    public Optional<Contract> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Contract> findByCompany(String company) {
        return findAll().stream()
                .filter(contract -> contract.getCompany().contains(company))
                .collect(Collectors.toList());
    }

    @Override
    public List<Contract> findByContractor(String contractor) {
        return findAll().stream()
                .filter(contract -> contract.getContractor().equals(contractor))
                .collect(Collectors.toList());
    }

    @Override
    public List<Contract> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public List<Contract> findByCompanyExact(String keyword) {
        return findByCompany(keyword);
    }

    @Override
    public List<Contract> findByCompanyContains(String keyword) {
        return null;
    }
}
