package net.neferett.tradingplugin.Trade.Price;

import lombok.*;
import net.neferett.tradingplugin.Trade.Enums.PriceEnum;
import net.neferett.tradingplugin.Trade.Enums.TradeType;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PriceAction {
    @NonNull
    private BigDecimal price;

    @NonNull
    private net.neferett.tradingplugin.Trade.Enums.PriceEnum type;

    @NonNull
    private Date createdAt;

    @NonNull
    private Date updatedAt;

    private boolean hit;

    private Date closedAt;

    private Float delta;

    public PriceAction(Float price, PriceEnum type) {
        this(new BigDecimal(price), type, new Date(), new Date(), false, null, null);
    }

    @SneakyThrows
    public PriceAction cloneAction() {
        return new PriceAction(
                this.price,
                this.type,
                this.getCreatedAt(),
                this.getUpdatedAt(),
                false,
                this.getClosedAt(),
                this.getDelta()
        );
    }

    public void calculDelta(BigDecimal price, TradeType type) {
        this.updatedAt = new Date();
        this.delta = (((price.floatValue()*100) / this.price.floatValue()) - 100) * (type == TradeType.BUY ? -1 : 1);
    }

    public boolean checkHit(PriceAction action, TradeType type) {
        return type == TradeType.BUY ? this.getPrice().floatValue() <= action.getPrice().floatValue() : this.getPrice().floatValue() >= action.getPrice().floatValue();
    }

}
