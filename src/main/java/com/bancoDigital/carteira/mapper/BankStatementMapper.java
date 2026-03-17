package com.bancoDigital.carteira.mapper;

import com.bancoDigital.carteira.domain.BankStatement;
import com.seuproject.model.BankStatementRepresentation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BankStatementMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "account", ignore = true)

    BankStatement toDomain(BankStatementRepresentation representation);

    BankStatementRepresentation toRepresentation(BankStatement bankStatement);

    List<BankStatementRepresentation> toRepresentationList(List<BankStatement> bankStatements);


}
