package com.example.savemoney.dto;

import lombok.Data;

@Data
public class GifticonInfoRequest {
    private byte[] gifticonImage;
    private String productName;
    private String code;
}
