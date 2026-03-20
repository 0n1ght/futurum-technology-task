package com.futurumtechtask.service.impl;

import com.futurumtechtask.dto.CampaignRequest;
import com.futurumtechtask.dto.CampaignResponse;
import com.futurumtechtask.entity.Account;
import com.futurumtechtask.entity.Campaign;
import com.futurumtechtask.mapper.CampaignMapper;
import com.futurumtechtask.repo.CampaignRepo;
import com.futurumtechtask.service.AccountService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CampaignServiceImplTest {

    @Mock
    private CampaignRepo campaignRepo;

    @Mock
    private AccountService accountService;

    @Mock
    private CampaignMapper campaignMapper;

    @InjectMocks
    private CampaignServiceImpl service;

    @Test
    void shouldCreateCampaign() {
        Account account = new Account();
        account.setEmeraldBalance(BigDecimal.valueOf(1000));

        CampaignRequest request = mock(CampaignRequest.class);
        when(request.getCampaignFund()).thenReturn(BigDecimal.valueOf(200));
        when(request.getKeywords()).thenReturn(List.of("a, b"));

        when(accountService.getCurrentAccount()).thenReturn(account);
        when(campaignMapper.toCampaignEntity(request)).thenReturn(new Campaign());
        when(campaignRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(campaignMapper.toCampaignResponse(any())).thenReturn(mock(CampaignResponse.class));

        CampaignResponse response = service.create(request);

        assertNotNull(response);
        assertEquals(BigDecimal.valueOf(800), account.getEmeraldBalance());
        verify(campaignRepo).save(any());
    }

    @Test
    void shouldThrowWhenNotEnoughFunds() {
        Account account = new Account();
        account.setEmeraldBalance(BigDecimal.valueOf(100));

        CampaignRequest request = mock(CampaignRequest.class);
        when(request.getCampaignFund()).thenReturn(BigDecimal.valueOf(200));

        when(accountService.getCurrentAccount()).thenReturn(account);

        assertThrows(IllegalArgumentException.class,
                () -> service.create(request));
    }

    @Test
    void shouldReturnAllCampaigns() {
        Account account = new Account();

        when(accountService.getCurrentAccount()).thenReturn(account);
        when(campaignRepo.findByAccount(account)).thenReturn(List.of(new Campaign()));
        when(campaignMapper.toCampaignResponse(any())).thenReturn(mock(CampaignResponse.class));

        List<CampaignResponse> result = service.getAll();

        assertEquals(1, result.size());
        verify(campaignRepo).findByAccount(account);
    }

    @Test
    void shouldUpdateCampaignIncreaseFund() {
        Account account = new Account();
        account.setId(1L);
        account.setEmeraldBalance(BigDecimal.valueOf(1000));

        Campaign campaign = new Campaign();
        campaign.setAccount(account);
        campaign.setCampaignFund(BigDecimal.valueOf(200));

        CampaignRequest request = mock(CampaignRequest.class);
        when(request.getCampaignFund()).thenReturn(BigDecimal.valueOf(400));
        when(request.getKeywords()).thenReturn(List.of("x"));

        when(accountService.getCurrentAccount()).thenReturn(account);
        when(campaignRepo.findById(1L)).thenReturn(Optional.of(campaign));
        when(campaignRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(campaignMapper.toCampaignResponse(any())).thenReturn(mock(CampaignResponse.class));

        CampaignResponse result = service.update(1L, request);

        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(800), account.getEmeraldBalance());
    }

    @Test
    void shouldUpdateCampaignDecreaseFund() {
        Account account = new Account();
        account.setId(1L);
        account.setEmeraldBalance(BigDecimal.valueOf(1000));

        Campaign campaign = new Campaign();
        campaign.setAccount(account);
        campaign.setCampaignFund(BigDecimal.valueOf(400));

        CampaignRequest request = mock(CampaignRequest.class);
        when(request.getCampaignFund()).thenReturn(BigDecimal.valueOf(200));
        when(request.getKeywords()).thenReturn(List.of("x"));

        when(accountService.getCurrentAccount()).thenReturn(account);
        when(campaignRepo.findById(1L)).thenReturn(Optional.of(campaign));
        when(campaignRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(campaignMapper.toCampaignResponse(any())).thenReturn(mock(CampaignResponse.class));

        service.update(1L, request);

        assertEquals(BigDecimal.valueOf(1200), account.getEmeraldBalance());
    }

    @Test
    void shouldThrowWhenAccessDenied() {
        Account account = new Account();
        account.setId(1L);

        Account other = new Account();
        other.setId(2L);

        Campaign campaign = new Campaign();
        campaign.setAccount(other);

        when(accountService.getCurrentAccount()).thenReturn(account);
        when(campaignRepo.findById(1L)).thenReturn(Optional.of(campaign));

        CampaignRequest request = mock(CampaignRequest.class);

        assertThrows(RuntimeException.class,
                () -> service.update(1L, request));
    }

    @Test
    void shouldDeleteCampaignAndRefund() {
        Account account = new Account();
        account.setId(1L);
        account.setEmeraldBalance(BigDecimal.valueOf(500));

        Campaign campaign = new Campaign();
        campaign.setAccount(account);
        campaign.setCampaignFund(BigDecimal.valueOf(200));

        when(accountService.getCurrentAccount()).thenReturn(account);
        when(campaignRepo.findById(1L)).thenReturn(Optional.of(campaign));

        service.delete(1L);

        assertEquals(BigDecimal.valueOf(700), account.getEmeraldBalance());
        verify(campaignRepo).delete(campaign);
    }

    @Test
    void shouldThrowWhenCampaignNotFound() {
        when(accountService.getCurrentAccount()).thenReturn(new Account());
        when(campaignRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> service.delete(1L));
    }
}
