package com.api.Ubank.Service;

import com.api.Ubank.Entity.Transaction;
import com.api.Ubank.Entity.User;
import com.api.Ubank.Repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private UserService userService;
    public Transaction createTransaction(String transactionType, double amount, Date transactionDate, User user) {
        Transaction transaction = new Transaction();
        transaction.setTransactionType(transactionType);
        transaction.setAmount(amount);
        transaction.setTransactionDate(transactionDate);
        transaction.setUser(user);
        if ("Depósito".equals(transactionType)) {
            user.setBalance(user.getBalance() + amount);
        } else if ("Retiro".equals(transactionType)) {
            user.setBalance(user.getBalance() - amount);
        } // Agrega más casos para otros tipos de transacción si es necesario

        transactionRepository.save(transaction);
        userService.updateUser(user);
        return transaction;
    }
    public Transaction createTransferTransaction(double amount, User sender, User receiver) {
        // Verifica que el usuario remitente tenga saldo suficiente para la transferencia
        if (sender.getBalance() >= amount) {
            // Realiza la transferencia descontando el monto del saldo del remitente
            sender.setBalance(sender.getBalance() - amount);
            receiver.setBalance(receiver.getBalance() + amount);

            // Crea una transacción para registrar la transferencia
            Transaction transaction = new Transaction();
            transaction.setTransactionType("Transferencia");
            transaction.setAmount(amount);
            transaction.setTransactionDate(new Date());
            transaction.setUser(sender);

            // Guarda la transacción y actualiza los saldos de los usuarios
            transactionRepository.save(transaction);
            userService.updateUser(sender);
            userService.updateUser(receiver);

            return transaction;
        } else {
            throw new RuntimeException("Saldo insuficiente para realizar la transferencia.");
        }
    }
    public List<Transaction> getTransactionsByUser(User user) {
        return transactionRepository.findByUser(user);
    }

}
