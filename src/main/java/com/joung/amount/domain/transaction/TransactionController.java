package com.joung.amount.domain.transaction;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/transactions")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class TransactionController {

    private TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping
    @ResponseBody
    public List<TransactionDto.Response> getTransactions() {
        List<Transaction> transactions = transactionService.getTransactions();
        return transactions.stream().map(TransactionDto.Response::new).collect(Collectors.toList());
    }

    @PostMapping
    @ResponseBody
    public TransactionDto.Response createTransaction(@RequestBody TransactionDto.Request request) {
        System.out.println(request.getDate() + " / " + request.getAmount() + " / " + request.getDescription());
        Transaction transaction = transactionService.addTransaction(request);
        return new TransactionDto.Response(transaction);
    }

}
