package com.joung.amount.domain.transaction;

import com.joung.amount.domain.user.User;
import com.joung.amount.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    public List<Transaction> getTransactions(String email) {
        User user = userRepository.findUserByEmail(email);
        return transactionRepository.findTransactionByUserId(user.getId());
    }

    public Transaction addTransaction(String email, TransactionDto.Request request) {
        User user = userRepository.findUserByEmail(email);
        Transaction transaction = request.asEntity(user);
        return transactionRepository.save(transaction);
    }

}
