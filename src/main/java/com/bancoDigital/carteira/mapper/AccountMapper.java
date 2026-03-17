package com.bancoDigital.carteira.mapper;

import com.bancoDigital.carteira.domain.Account;
import com.seuproject.model.AccountRequest;
import com.seuproject.model.AccountResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "balance", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Account toDomain(AccountRequest request);

    AccountResponse toResponse(Account account);

    List<AccountResponse> toResponseList(List<Account> accounts);

    default OffsetDateTime map(LocalDateTime value) {
        if (value == null) return null;
        return value.atOffset(ZoneOffset.UTC);
    }

}
