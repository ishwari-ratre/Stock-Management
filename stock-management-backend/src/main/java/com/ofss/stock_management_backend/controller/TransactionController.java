package com.ofss.stock_management_backend.controller;

import com.ofss.stock_management_backend.dto.TradeRequest;
import com.ofss.stock_management_backend.model.Transaction;
import com.ofss.stock_management_backend.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/buy")
    public ResponseEntity<?> buyStock(@RequestBody TradeRequest tradeRequest) {
        try {
            Transaction transaction = transactionService.buyStock(tradeRequest);
            return ResponseEntity.ok(transaction);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Something went wrong while buying stock");
        }
    }

    @PostMapping("/sell")
    public ResponseEntity<?> sellStock(@RequestBody TradeRequest tradeRequest) {
        try {
            Transaction transaction = transactionService.sellStock(tradeRequest);
            return ResponseEntity.ok(transaction);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Something went wrong while selling stock");
        }
    }
}
