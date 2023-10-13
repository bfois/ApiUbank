package com.api.Ubank.Service;

import com.api.Ubank.DTO.UserDTO;
import com.api.Ubank.Entity.Card;
import com.api.Ubank.Entity.User;
import com.api.Ubank.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Random;
import java.util.UUID;

@Service
public class UserCardService {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CardService cardService;


    public User createUserWithRandomCard(UserDTO newUser) {
        // Genera un número de tarjeta de crédito aleatorio de 16 dígitos
        String cardNumber = generateRandomCardNumber();
        String cvuNumber = generateUniqueCVU();
        // Genera una fecha de vencimiento aleatoria (puedes especificar un rango de fechas si lo deseas)
        String expirationDate = generateRandomExpirationDate();

        // Crea un usuario con los datos proporcionados
        User user = new User();
        user.setFirstName(newUser.getFirstName());
        user.setLastName(newUser.getLastName());
        user.setDni(newUser.getDni());
        user.setCvu(cvuNumber);
        user.setEmail(newUser.getEmail());
        user.setPassword(newUser.getPassword());
        user.setBalance(0.0); // Puedes establecer el saldo inicial aquí

        // Crea una tarjeta de crédito con el número y fecha generados y deshabilitada por defecto
        Card card = new Card();
        card.setCardNumber(cardNumber);
        card.setExpirationDate(expirationDate);
        card.setBalance(0.0); // Puedes establecer el saldo inicial aquí
        card.setActivated(false);

        // Asocia la tarjeta de crédito al usuario
        user.setCards(Collections.singletonList(card));

        // Guarda el usuario y la tarjeta de crédito en la base de datos
        User createdUser = userService.createUser(user);
        card.setUser(createdUser);
        cardService.createCard(card);

        return createdUser;
    }
    private String generateRandomCardNumber() {
        StringBuilder cardNumber = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 16; i++) {
            int digit = random.nextInt(10); // Genera un dígito aleatorio (0-9)
            cardNumber.append(digit);
        }

        return cardNumber.toString();
    }
    private String generateRandomExpirationDate() {
        // Obtén el año actual
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);

        // Genera un año aleatorio dentro de un rango de 5 años (actual + 1 a actual + 5)
        int randomYear = currentYear + new Random().nextInt(5) + 1;

        // Genera un mes aleatorio (1-12)
        int randomMonth = new Random().nextInt(12) + 1;

        // Formatea la fecha de vencimiento en el formato MM/yy
        DecimalFormat twoDigitFormat = new DecimalFormat("00");
        String formattedMonth = twoDigitFormat.format(randomMonth);
        String formattedYear = String.valueOf(randomYear).substring(2);

        return formattedMonth + "/" + formattedYear;
    }
    private String generateUniqueCVU() {
        String cvu = UUID.randomUUID().toString().replace("-", "").substring(0, 16);
        while (userRepository.existsByCvu(cvu)) {
            cvu = UUID.randomUUID().toString().replace("-", "").substring(0, 16);
        }

        return cvu;
    }
}
