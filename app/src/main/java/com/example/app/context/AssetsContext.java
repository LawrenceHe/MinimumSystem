package com.example.app.context;

import com.example.app.provider.UserAssetProvider;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.text.DecimalFormat;

@Data
@Slf4j
public class AssetsContext {

    private UserAssetProvider provider;
    private DecimalFormat df;
    private Long id;
    private String fortune;
    private String loan;

    public AssetsContext(Long id, UserAssetProvider provider) {
        this.id = id;
        this.provider = provider;
        this.df = new DecimalFormat();
        df.applyPattern("###,###,###.00");
    }

    public String getFortune() {
        try {
            String fortuneString = provider.getFortuneById(id);
            this.fortune = df.format(new BigDecimal(fortuneString));
        } catch (Exception e) {
            log.error(e.getMessage());
            this.fortune = "0";
        }

        return fortune;
    }

    public String getLoan() {
        try {
            String loanString = provider.getFortuneById(id);
            this.loan = df.format(new BigDecimal(loanString));
        } catch (Exception e) {
            log.error(e.getMessage());
            this.loan= "0";
        }

        return loan;
    }
}
