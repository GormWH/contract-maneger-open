package com.contractmanager6.api.contract;

import com.contractmanager6.api.jwt.JwtUtils;
import com.contractmanager6.api.request.ContractRequest;
import com.contractmanager6.api.response.*;
import com.contractmanager6.domain.contract.Contract;
import com.contractmanager6.domain.contract.ContractRepository;
import com.contractmanager6.domain.user.User;
import com.contractmanager6.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@Transactional
public class ContractController {

    private final ContractRepository contractRepository;
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;

    @GetMapping("/contract")
    public ContractListResponse list(
            @RequestParam(value = "keyword", required = false)String keyword,
            @RequestParam(value = "exact", required = false)Boolean exact
    ) {
        ContractListResponse result = new ContractListResponse();
        if (keyword == null || keyword.isBlank()) {
            result.setContracts(contractRepository.findAll().stream().map(contract -> {
                ContractResponse contractResponse = new ContractResponse();
                contractResponse.setId(contract.getId());
                contractResponse.setCompany(contract.getCompany());
                contractResponse.setContractor(new UserName(contract.getContractor()));
                contractResponse.setTimestamp(contract.getTimestamp());
                return contractResponse;
            }).collect(Collectors.toList()));
            return result;
        }
        if (exact == null || !exact) {
            result.setContracts(contractRepository.findByCompanyContains(keyword).stream().map(contract -> {
                ContractResponse contractResponse = new ContractResponse();
                contractResponse.setId(contract.getId());
                contractResponse.setCompany(contract.getCompany());
                contractResponse.setContractor(new UserName(contract.getContractor()));
                contractResponse.setTimestamp(contract.getTimestamp());
                return contractResponse;
            }).collect(Collectors.toList()));
            return result;
        }
        result.setContracts(contractRepository.findByCompanyExact(keyword).stream().map(contract -> {
            ContractResponse contractResponse = new ContractResponse();
            contractResponse.setId(contract.getId());
            contractResponse.setCompany(contract.getCompany());
            contractResponse.setContractor(new UserName(contract.getContractor()));
            contractResponse.setTimestamp(contract.getTimestamp());
            return contractResponse;
        }).collect(Collectors.toList()));
        return result;
    }

    @PostMapping("/contract")
    public ResponseEntity<MessageResponse> register(@RequestBody ContractRequest contractRequest) {
        log.info("register: {}", contractRequest);
        if (!contractRepository.findByCompany(contractRequest.getCompany()).isEmpty()) {
            return ResponseEntity.status(400).body(new MessageResponse("company duplicated"));
        }
        if (userRepository.findByLoginId(contractRequest.getContractor()).isEmpty()) {
            return ResponseEntity.status(400).body(new MessageResponse("contractor not valid"));
        }
        Contract contract = new Contract();
        contract.setContractor(contractRequest.getContractor());
        contract.setCompany(contractRequest.getCompany());
        contract.setTimestamp(contractRequest.getTimestamp());
        contractRepository.save(contract);
        return ResponseEntity.ok(new MessageResponse("success"));
    }

    @DeleteMapping("/contract")
    public ResponseEntity<MessageResponse> delete(@RequestParam("id")Long contractId, HttpServletRequest request) {
        log.info("delete");
        Optional<Contract> targetOptional = contractRepository.findById(contractId);
        if (targetOptional.isEmpty()) {
            return ResponseEntity.status(400).body(new MessageResponse("contract doesn't exist"));
        }
        Contract contract = targetOptional.get();
        String accessUserId = jwtUtils.getLoginIdFromJwtToken(request.getHeader("Authorization"));
        if (contract.getContractor().equals(accessUserId)) {
            contractRepository.deleteById(contractId);
            return ResponseEntity.ok(new MessageResponse("success"));
        }
        return ResponseEntity.status(400).body(new MessageResponse("invalid user"));
    }

    @DeleteMapping("/admin/contract")
    public ResponseEntity<MessageResponse> adminDelete(@RequestParam("id")Long contractId) {
        log.info("admin delete");
        if (contractRepository.findById(contractId).isEmpty()) {
            return ResponseEntity.status(400).body(new MessageResponse("contract doesn't exist"));
        }
        contractRepository.deleteById(contractId);
        return ResponseEntity.ok(new MessageResponse("success"));
    }
}
