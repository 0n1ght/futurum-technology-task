package com.futurumtechtask.service;

import com.futurumtechtask.dto.CampaignRequest;
import com.futurumtechtask.dto.CampaignResponse;

import java.util.List;

public interface CampaignService {
    CampaignResponse create(CampaignRequest request);
    List<CampaignResponse> getAll();
    CampaignResponse update(Long id, CampaignRequest request);
    void delete(Long id);
}
