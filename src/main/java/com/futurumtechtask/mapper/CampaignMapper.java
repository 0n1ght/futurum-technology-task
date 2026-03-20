package com.futurumtechtask.mapper;

import com.futurumtechtask.dto.CampaignRequest;
import com.futurumtechtask.dto.CampaignResponse;
import com.futurumtechtask.entity.Campaign;
import org.springframework.stereotype.Component;

@Component
public class CampaignMapper {

    public Campaign toCampaignEntity(CampaignRequest request) {
        if (request == null) {
            return null;
        }

        return Campaign.builder()
                .name(request.getName())
                .keywords(request.getKeywords())
                .bidAmount(request.getBidAmount())
                .campaignFund(request.getCampaignFund())
                .status(request.getStatus())
                .town(request.getTown())
                .radius(request.getRadius())
                .build();
    }

    public CampaignResponse toCampaignResponse(Campaign campaign) {
        if (campaign == null) {
            return null;
        }

        return new CampaignResponse(
                campaign.getId(),
                campaign.getName(),
                campaign.getKeywords(),
                campaign.getBidAmount(),
                campaign.getCampaignFund(),
                campaign.getStatus(),
                campaign.getTown(),
                campaign.getRadius()
        );
    }
}
