package com.joung.amount.domain.transaction;

import com.joung.amount.domain.user.User;
import com.joung.amount.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public List<Transaction> getTransactions(String email, String stringDate) {
        User user = userRepository.findUserByEmail(email);
        if (stringDate != null) {
            LocalDate date = LocalDate.parse(stringDate, formatter);
            return transactionRepository.findTransactionByUserIdAndDateBetween(user.getId(), date.withDayOfMonth(1), date.withDayOfMonth(date.lengthOfMonth()));
        } else {
            return transactionRepository.findTransactionByUserId(user.getId());
        }
    }

    public Transaction addTransaction(String email, TransactionDto.Request request) {
        User user = userRepository.findUserByEmail(email);
        Transaction transaction = request.asEntity(user);
        return transactionRepository.save(transaction);
    }

}
