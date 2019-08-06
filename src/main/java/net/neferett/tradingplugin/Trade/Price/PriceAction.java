package net.neferett.tradingplugin.Trade.Price;

import lombok.*;
import net.neferett.tradingplugin.Trade.Trade;
import net.neferett.tradingplugin.Trade.TradeType;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PriceAction {
    @NonNull
    private BigDecimal price;

    @NonNull
    private PriceEnum type;

    @NonNull
    private Date createdAt;

    @NonNull
    private Date updatedAt;

    private Date closedAt;

    private Float delta;

    public PriceAction(Float price, PriceEnum type) {
        this(new BigDecimal(price), type, new Date(), new Date(), null, null);
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

    public void calculDelta(BigDecimal price, TradeType type) {
        this.updatedAt = new Date();
        this.delta = (((price.floatValue()*100) / this.price.floatValue()) - 100) * (type == TradeType.BUY ? -1 : 1);
    }

    public boolean checkHit(PriceAction action, TradeType type) {
        System.out.println(type + " " + this.price + " cp " + action.getPrice() + " rs : " + this.price.compareTo(action.getPrice()));
        return (type == TradeType.SELL ? this.price.compareTo(action.getPrice()) : action.getPrice().compareTo(this.price)) <= 0;
    }

}
