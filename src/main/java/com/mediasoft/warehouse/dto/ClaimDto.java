package com.mediasoft.warehouse.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClaimDto {
    private String login;
    private String businessKey;
    private String CRM;
}
