package net.neferett.tradingplugin.Trade.Price;

import lombok.*;

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

    public PriceAction(Float price, PriceEnum type) {
        this(price, type, new Date(), new Date(), null);
    }

    @SneakyThrows
    public PriceAction cloneAction() {
        return new PriceAction(this.price, this.type, this.getCreatedAt(), this.getUpdatedAt(), this.getClosedAt());
    }
}
