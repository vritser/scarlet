package io.codewar.scarlet.vo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.codewar.scarlet.infra.annotation.QueryParams;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * BaseQueryParams
 */
@Data
@Schema(name = "分页参数")
public class BaseQueryParams {
    @Schema(title = "页数, 起始 1")
    @QueryParams("p")
    private Integer page = 1;

    @Schema(title = "每页条数, 默认 10")
    @QueryParams("ps")
    private Integer pageSize = 10;

    public <T> Page<T> toPage() {
        return new Page<>(page, pageSize);
    }

}
