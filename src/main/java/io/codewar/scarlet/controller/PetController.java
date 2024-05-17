package io.codewar.scarlet.controller;

import io.codewar.scarlet.infra.annotation.QueryParams;
import io.codewar.scarlet.infra.model.ErrorCode;
import io.codewar.scarlet.infra.model.Resp;
import io.codewar.scarlet.service.PetService;
import io.codewar.scarlet.vo.PetCreateForm;
import io.codewar.scarlet.vo.PetQueryParams;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.vavr.control.Either;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("pets")
@Tag(name = "Pets", description = "Pet API")
public class PetController {

    @Autowired
    private PetService petService;

    @GetMapping
    @Operation(summary = "Find pets")
    public Resp<?> find(@QueryParams PetQueryParams params) {
        return petService.find(params);
    }

    @PostMapping
    @Operation(summary = "Create pet")
    public Either<ErrorCode, ?> create(@RequestBody PetCreateForm form) {
        return petService.create(form);
    }
}
