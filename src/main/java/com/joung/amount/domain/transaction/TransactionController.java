package com.joung.amount.domain.transaction;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/transactions")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping
    public List<TransactionDto.Response> getTransactions(Principal principal) {
        List<Transaction> transactions = transactionService.getTransactions(principal.getName());
        return transactions.stream().map(TransactionDto.Response::new).collect(Collectors.toList());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TransactionDto.Response createTransaction(Principal principal, @RequestBody TransactionDto.Request request) {
        Transaction transaction = transactionService.addTransaction(principal.getName(), request);
        return new TransactionDto.Response(transaction);
    }

}
