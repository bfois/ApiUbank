package com.api.Ubank.Service;

import com.api.Ubank.Entity.Card;
import com.api.Ubank.Repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CardService {
    @Autowired
    private CardRepository cardRepository;

    public Card createCard(Card card ) {
        return cardRepository.save(card);
    }
    public List<Card> getCardsByUserId(Long userId) {
        return cardRepository.findByUserId(userId);
    }
    public Card activateCard(Long cardId) {
        Optional<Card> optionalCard = cardRepository.findById(cardId);
        if (optionalCard.isPresent()) {
            Card card = optionalCard.get();
            if (!card.isActivated()) {
                card.setActivated(true);
                return cardRepository.save(card);
            } else {
                throw new RuntimeException("La tarjeta ya está activada.");
            }
        } else {
            throw new RuntimeException("La tarjeta no existe en la base de datos.");
        }
    }
    public void deleteCard(Long cardId) {
        if (cardRepository.existsById(cardId)) {
            cardRepository.deleteById(cardId);
        } else {
            throw new RuntimeException("La tarjeta no existe en la base de datos.");
        }
    }

}
