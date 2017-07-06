package ea.itrade.duk.dto;

import lombok.Builder;
import lombok.Data;

/**
 * Created by wangliang on 2017/7/6.
 */
@Data
@Builder
public class TimePriceDto {
    public long time;
    public double price;
}
