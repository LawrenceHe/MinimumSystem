package com.example.app.context;

import com.example.app.provider.UserAssetProvider;
import lombok.Data;

import java.math.BigDecimal;
import java.text.DecimalFormat;

@Data
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
        String fortuneString = provider.getFortuneById(id);
        this.fortune = df.format(new BigDecimal(fortuneString));

        return fortune;
    }

    public String getLoan() {
        String loanString = provider.getFortuneById(id);
        this.loan = df.format(new BigDecimal(loanString));

        return loan;
    }
}
