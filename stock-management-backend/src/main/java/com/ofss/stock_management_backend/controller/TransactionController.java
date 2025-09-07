package com.ofss.stock_management_backend.controller;

import com.ofss.stock_management_backend.dto.PortfolioResponse;
import com.ofss.stock_management_backend.dto.TradeRequest;
import com.ofss.stock_management_backend.model.Transaction;
import com.ofss.stock_management_backend.service.TransactionService;
import com.ofss.stock_management_backend.util.JwtUtil;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {

    private final TransactionService transactionService;
    private final JwtUtil jwtUtil;

    public TransactionController(TransactionService transactionService, JwtUtil jwtUtil) {
        this.transactionService = transactionService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/buy")
    public ResponseEntity<?> buyStock(@RequestBody TradeRequest tradeRequest) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName(); // Extracted from JWT subject
        tradeRequest.setEmail(email);//put email extracted from token
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
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName(); // Extracted from JWT subject
        tradeRequest.setEmail(email);//put email extracted from token
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

    // Get all transaction history
    @GetMapping("/history")
    public ResponseEntity<List<Transaction>> getTransactionHistory(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String email = jwtUtil.extractEmail(token);
        return ResponseEntity.ok(transactionService.getTransactionHistory(email));
    }

    // Get portfolio (current holdings)
    @GetMapping("/portfolio")
    public ResponseEntity<List<PortfolioResponse>> getPortfolio(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String email = jwtUtil.extractEmail(token);
        return ResponseEntity.ok(transactionService.getPortfolioDetails(email));
    }
}
