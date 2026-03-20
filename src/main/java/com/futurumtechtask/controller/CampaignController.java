package com.futurumtechtask.controller;

import com.futurumtechtask.dto.CampaignRequest;
import com.futurumtechtask.dto.CampaignResponse;
import com.futurumtechtask.service.CampaignService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/campaigns")
@RequiredArgsConstructor
public class CampaignController {
    private final CampaignService campaignService;

    @PostMapping
    public CampaignResponse create(@Valid @RequestBody CampaignRequest request) {
        return campaignService.create(request);
    }

    @GetMapping
    public List<CampaignResponse> getAll() {
        return campaignService.getAll();
    }

    @PutMapping("/{id}")
    public CampaignResponse update(@PathVariable Long id,
                           @Valid @RequestBody CampaignRequest request) {
        return campaignService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        campaignService.delete(id);
    }
}
