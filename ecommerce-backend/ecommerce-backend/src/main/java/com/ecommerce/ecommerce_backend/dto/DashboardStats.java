package com.ecommerce.ecommerce_backend.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DashboardStats {
    private long totalOrders;
    private double totalRevenue;
    private long pendingOrders;
    private long confirmedOrders;
    private long deliveredOrders;
}