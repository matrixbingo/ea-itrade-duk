package ea.itrade.duk.dto;

import ea.itrade.duk.enums.DeviateTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by wangliang on 2017/7/6.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviateDto {
    private DeviateTypeEnum deviateTypeEnum;
    private long time;
}
