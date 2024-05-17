package io.codewar.scarlet.service;

import io.codewar.scarlet.infra.model.ErrorCode;
import io.codewar.scarlet.infra.model.Resp;
import io.codewar.scarlet.vo.PetCreateForm;
import io.codewar.scarlet.vo.PetQueryParams;
import io.vavr.control.Either;

public interface PetService {
    Resp<?> find(PetQueryParams params);

    Either<ErrorCode,?> create(PetCreateForm form);
}
