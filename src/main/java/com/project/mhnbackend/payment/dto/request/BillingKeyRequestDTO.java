package com.project.mhnbackend.payment.dto.request;

import lombok.Getter;

import java.util.List;

@Getter
public class BillingKeyRequestDTO {
    private String customer_uid; //필수
//    private String customer_id;
//    private Integer checking_amount;
//    private String card_number;
//    private String expiry;
//    private String birth;
//    private String pwd_2digit;
//    private String cvc;
//    private String pg;
    private List<Schedule> schedules;
    
    @Getter
    public static class Schedule {
        private String merchant_uid;
        private long schedule_at;
        private String currency;
        private double amount;
//        private Double tax_free;
//        private Double vat_amount;
        private String name;
        private String buyer_name;
        private String buyer_email;
        private String buyer_tel;
//        private String buyer_addr;
//        private String buyer_postcode;
//        private String custom_data;
//        private String notice_url;
//        private String product_type;
//        private String cash_receipt_type;
//        private Integer card_quota;
//        private Boolean interest_free_by_merchant;
//        private Boolean use_card_point;
//        private Integer product_count;
//        private Object extra;
//        private String bypass;


    }

}
