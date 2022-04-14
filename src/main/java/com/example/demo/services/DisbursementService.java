package com.example.demo.services;

import com.example.demo.model.Disbursements;
import com.example.demo.persistence.repository.DisbursementsRepo;
import org.springframework.stereotype.Service;

@Service
public class DisbursementService {
    public final DisbursementsRepo disbursementsRepo;

    public DisbursementService(DisbursementsRepo disbursementsRepo) {
        this.disbursementsRepo = disbursementsRepo;

    }
    public Disbursements saveDisbursement(Disbursements disbursement){
        return disbursementsRepo.save(disbursement);
    }

}
