package ea.itrade.duk.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by wangliang on 2017/6/20.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MacdDto {
    private int shift;
    private double[] macd;
}
