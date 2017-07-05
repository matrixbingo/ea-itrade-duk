package ea.itrade.duk.dto;

import com.dukascopy.api.IBar;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by wangliang on 2017/7/5.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CandleDto {
    private long time;
    private int shift;
    private IBar bar;
    private double hist;
    private String histStr;
}
