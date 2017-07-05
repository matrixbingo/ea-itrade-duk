package ea.itrade.duk.dto;

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
public class MaxMinDto {
    private double max;
    private double min;
}
