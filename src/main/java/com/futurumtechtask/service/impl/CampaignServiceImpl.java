package com.futurumtechtask.service.impl;

import com.futurumtechtask.dto.CampaignRequest;
import com.futurumtechtask.dto.CampaignResponse;
import com.futurumtechtask.entity.Campaign;
import com.futurumtechtask.entity.Account;
import com.futurumtechtask.mapper.CampaignMapper;
import com.futurumtechtask.repo.CampaignRepo;
import com.futurumtechtask.service.AccountService;
import com.futurumtechtask.service.CampaignService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CampaignServiceImpl implements CampaignService {

    private final CampaignRepo campaignRepo;
    private final AccountService accountService;
    private final CampaignMapper campaignMapper;

    @Override
    @Transactional
    public CampaignResponse create(CampaignRequest request) {
        Account account = accountService.getCurrentAccount();

        if (account.getEmeraldBalance().compareTo(request.getCampaignFund()) < 0) {
            throw new IllegalArgumentException("Not enough funds");
        }

        account.setEmeraldBalance(
                account.getEmeraldBalance().subtract(request.getCampaignFund())
        );

        Campaign campaign = campaignMapper.toCampaignEntity(request);

        campaign.setKeywords(normalizeKeywords(request.getKeywords()));

        campaign.setAccount(account);

        Campaign saved = campaignRepo.save(campaign);

        return campaignMapper.toCampaignResponse(saved);
    }

    @Override
    public List<CampaignResponse> getAll() {
        Account account = accountService.getCurrentAccount();

        return campaignRepo.findByAccount(account)
                .stream()
                .map(campaignMapper::toCampaignResponse)
                .toList();
    }

    @Override
    @Transactional
    public CampaignResponse update(Long id, CampaignRequest request) {
        Account account = accountService.getCurrentAccount();

        Campaign campaign = campaignRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Campaign not found"));

        if (!campaign.getAccount().getId().equals(account.getId())) {
            throw new RuntimeException("Access denied");
        }

        BigDecimal oldFund = campaign.getCampaignFund();
        BigDecimal newFund = request.getCampaignFund();
        BigDecimal diff = newFund.subtract(oldFund);

        if (diff.compareTo(BigDecimal.ZERO) > 0) {
            if (account.getEmeraldBalance().compareTo(diff) < 0) {
                throw new IllegalArgumentException("Not enough funds");
            }
            account.setEmeraldBalance(account.getEmeraldBalance().subtract(diff));
        }

        if (diff.compareTo(BigDecimal.ZERO) < 0) {
            account.setEmeraldBalance(account.getEmeraldBalance().add(diff.abs()));
        }

        campaign.setName(request.getName());
        campaign.setKeywords(normalizeKeywords(request.getKeywords()));
        campaign.setBidAmount(request.getBidAmount());
        campaign.setCampaignFund(newFund);
        campaign.setStatus(request.getStatus());
        campaign.setTown(request.getTown());
        campaign.setRadius(request.getRadius());

        Campaign updated = campaignRepo.save(campaign);

        return campaignMapper.toCampaignResponse(updated);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Account account = accountService.getCurrentAccount();

        Campaign campaign = campaignRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Campaign not found"));

        if (!campaign.getAccount().getId().equals(account.getId())) {
            throw new RuntimeException("Access denied");
        }

        account.setEmeraldBalance(
                account.getEmeraldBalance().add(campaign.getCampaignFund())
        );

        campaignRepo.delete(campaign);
    }

    private List<String> normalizeKeywords(List<String> keywords) {
        if (keywords == null) {
            return List.of();
        }

        return keywords.stream()
                .flatMap(k -> Arrays.stream(k.split(",")))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
    }
}