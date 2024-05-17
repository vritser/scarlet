package io.codewar.scarlet.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.codewar.scarlet.dao.PetDao;
import io.codewar.scarlet.entity.Pet;
import io.codewar.scarlet.infra.extension.OptionalLambdaQueryWrapper;
import io.codewar.scarlet.infra.model.ErrorCode;
import io.codewar.scarlet.infra.model.Resp;
import io.codewar.scarlet.service.PetService;
import io.codewar.scarlet.vo.PetCreateForm;
import io.codewar.scarlet.vo.PetQueryParams;
import io.vavr.control.Either;
import io.vavr.control.Option;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PetServiceImpl implements PetService {

    @Autowired
    private PetDao petDao;

    @Override
    public Resp<?> find(PetQueryParams params) {
        var query = new OptionalLambdaQueryWrapper<Pet>()
                .eqG(Pet::getId, params.getId())
                .likeG(Pet::getName, params.getName())
                .betweenG(Pet::getBirthDate, params.getBirthDate())
                .orderByDesc(Pet::getId);

        var page = petDao.selectPage(params.toPage(), query);
        return Resp.from(page);
    }

    @Override
    public Either<ErrorCode, ?> create(PetCreateForm form) {
        var query = new LambdaQueryWrapper<Pet>()
                .like(Pet::getName, form.getName());

        return Option.when(petDao.selectCount(query) <= 0, () -> new Pet().setName(form.getName()).setBirthDate(form.getBirthDate()))
                .toEither(ErrorCode.BadRequest)
                .map(pet -> {
                    petDao.insert(pet);
                    return pet.getId();
                });
    }
}
