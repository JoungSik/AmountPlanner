package com.joung.amount.domain.transaction;

import com.joung.amount.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TransactionDto {

    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Getter
    @Setter
    public static class Request {
        private Long id;
        private String date;
        private int amount;
        private String description;

        public Transaction asEntity(User user) {
            return Transaction.builder()
                    .user(user)
                    .data(LocalDate.parse(date, formatter))
                    .amount(amount)
                    .description(description)
                    .build();
        }
    }

    @Getter
    @AllArgsConstructor
    public static class Response {
        private Long id;
        private String date;
        private int amount;
        private String description;

        public Response(Transaction transaction) {
            id = transaction.getId();
            date = transaction.getDate().format(formatter);
            amount = transaction.getAmount();
            description = transaction.getDescription();
        }
    }

}
