package com.futurumtechtask.repo;

import com.futurumtechtask.entity.Account;
import com.futurumtechtask.entity.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CampaignRepo extends JpaRepository<Campaign, Long> {
    List<Campaign> findByAccount(Account account);
}
