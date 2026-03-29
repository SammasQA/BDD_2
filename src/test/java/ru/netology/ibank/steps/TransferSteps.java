package ru.netology.ibank.steps;

import io.cucumber.java.ru.Когда;
import io.cucumber.java.ru.Пусть;
import io.cucumber.java.ru.Тогда;
import ru.netology.ibank.data.DataHelper;
import ru.netology.ibank.page.DashboardPage;
import ru.netology.ibank.page.LoginPage;
import ru.netology.ibank.page.TransferPage;
import ru.netology.ibank.page.VerificationPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TransferSteps {

    private static final String SUT_URL = "http://localhost:9999";
    private DashboardPage dashboardPage;

    @Пусть("пользователь залогинен с именем {string} и паролем {string}")
    public void login(String login, String password) {
        open(SUT_URL);
        LoginPage loginPage = new LoginPage();
        VerificationPage verificationPage = loginPage.validLogin(login, password);

        dashboardPage = verificationPage.validVerify(DataHelper.getValidVerificationCode());
    }

    @Когда("пользователь переводит {int} рублей с карты с номером {string} на карту с номером {string}")
    public void transfer(int amount, String fromCardNumber, String toCardNumber) {
        // Определяем индекс карты-получателя по последним 4 цифрам
        int receiverIndex = findCardIndexByLastFourDigits(toCardNumber);
        TransferPage transferPage = dashboardPage.clickTransferButton(receiverIndex);
        dashboardPage = transferPage.transfer(amount, fromCardNumber);
    }

    @Тогда("баланс карты с номером {string} должен стать {int} рублей")
    public void verifyBalance(String cardNumber, int expectedBalance) {
        int cardIndex = findCardIndexByLastFourDigits(cardNumber);
        int actualBalance = dashboardPage.getCardBalance(cardIndex);
        assertEquals(expectedBalance, actualBalance,
                "Баланс карты " + cardNumber + " не соответствует ожидаемому");
    }


    private int findCardIndexByLastFourDigits(String fullCardNumber) {
        String cleaned = fullCardNumber.replaceAll("\\s+", "");
        String lastFour = cleaned.substring(cleaned.length() - 4);

        int cardsCount = dashboardPage.getCardsCount();
        for (int i = 0; i < cardsCount; i++) {
            String cardText = dashboardPage.getCardText(i);
            if (cardText.contains(lastFour)) {
                return i;
            }
        }
        throw new IllegalArgumentException(
                "Карта с номером, оканчивающимся на " + lastFour + ", не найдена на Dashboard"
        );
    }
}