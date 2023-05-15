package com.mzw.dto;

import com.mzw.entity.OrderDetail;
import com.mzw.entity.Orders;
import com.mzw.entity.OrderDetail;
import com.mzw.entity.Orders;
import lombok.Data;
import java.util.List;

@Data
public class OrdersDto extends Orders {

    private String userName;

    private String phone;

    private String address;

    private String consignee;

    private List<OrderDetail> orderDetails;
	
}
