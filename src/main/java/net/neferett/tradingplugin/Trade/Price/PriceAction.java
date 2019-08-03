package net.neferett.tradingplugin.Trade.Price;

import lombok.*;
import net.neferett.tradingplugin.Trade.Trade;
import net.neferett.tradingplugin.Trade.TradeType;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PriceAction {
    @NonNull
    private Float price;

    @NonNull
    private PriceEnum type;

    @NonNull
    private Date createdAt;

    @NonNull
    private Date updatedAt;

    private Date closedAt;

    private Float delta;

    public PriceAction(Float price, PriceEnum type) {
        this(price, type, new Date(), new Date(), null, null);
    }

    @SneakyThrows
    public PriceAction cloneAction() {
        return new PriceAction(
                this.price,
                this.type,
                this.getCreatedAt(),
                this.getUpdatedAt(),
                this.getClosedAt(),
                this.getDelta()
        );
    }

    public void calculDelta(Float price, TradeType type) {
        this.delta = (((price*100) / this.price) - 100) * (type == TradeType.BUY ? -1 : 1);
    }
}
