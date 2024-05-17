package io.codewar.scarlet.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.codewar.scarlet.entity.Pet;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PetDao extends BaseMapper<Pet> {
}
