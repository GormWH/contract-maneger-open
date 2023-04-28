package com.contractmanager6.domain.contract;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface JpaDataContractRepository extends ContractRepository, JpaRepository<Contract, Long> {

    @Override
    @Query("select c from Contract c where lower(replace(c.company,' ','')) = lower(replace(:keyword,' ',''))")
    List<Contract> findByCompanyExact(String keyword);


}
