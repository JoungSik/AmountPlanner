package com.joung.amount.domain.user;

import com.joung.amount.domain.transaction.TransactionDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

public class UserDto {

    @Getter
    @Setter
    public static class Request {
        private Long id;
        private String email;
        private String password;
    }

    @Getter
    @AllArgsConstructor
    public static class Response {
        private Long id;
        private String email;
        private List<TransactionDto.Response> transactions;

        public Response(User user) {
            id = user.getId();
            email = user.getEmail();

            if (user.getTransactions() != null) {
                transactions = user.getTransactions()
                        .stream()
                        .map(TransactionDto.Response::new)
                        .collect(Collectors.toList());
            }
        }
    }

}
