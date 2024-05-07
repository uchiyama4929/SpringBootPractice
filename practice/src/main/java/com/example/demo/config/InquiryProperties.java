package com.example.demo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import java.util.Map;
import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "inquiry")
public class InquiryProperties {
    /** types お問い合わせ種別 */
    private Map<String, String> types;
}
