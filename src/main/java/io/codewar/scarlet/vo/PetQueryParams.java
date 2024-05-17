package io.codewar.scarlet.vo;

import io.codewar.scarlet.infra.model.DateRange;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Optional;

@Data
@EqualsAndHashCode(callSuper = true)
public class PetQueryParams extends BaseQueryParams {
    private Optional<Long> id = Optional.empty();
    private Optional<String> name = Optional.empty();
    private Optional<DateRange> birthDate = Optional.empty();
}
