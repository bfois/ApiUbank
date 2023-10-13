package com.api.Ubank.Controller;

import com.api.Ubank.Entity.Card;
import com.api.Ubank.Service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "http://localhost:4200/")
public class CardController {
    @Autowired
    private CardService cardService;

    @GetMapping("/users/{userId}/cards")
    public ResponseEntity<List<Card>> getAllCardsByUserId(@PathVariable Long userId) {
        List<Card> cards = cardService.getCardsByUserId(userId);
        return ResponseEntity.ok(cards);
    }
    @PutMapping("/cards/{cardId}/activate")
    public ResponseEntity<Card> activateCard(@PathVariable Long cardId) {
        Card activatedCard = cardService.activateCard(cardId);
        if (activatedCard != null) {
            return ResponseEntity.ok(activatedCard);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
