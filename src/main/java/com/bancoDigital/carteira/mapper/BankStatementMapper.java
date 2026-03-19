package com.bancoDigital.carteira.mapper;

import com.bancoDigital.carteira.domain.BankStatement;
import com.seuproject.model.BankStatementRepresentation;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BankStatementMapper {


    BankStatement toDomain(BankStatementRepresentation representation);

    BankStatementRepresentation toRepresentation(BankStatement bankStatement);

    List<BankStatementRepresentation> toRepresentationList(List<BankStatement> bankStatements);


}
