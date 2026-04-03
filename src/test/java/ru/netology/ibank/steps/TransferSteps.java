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
        TransferPage transferPage = dashboardPage.clickTransferButton(toCardNumber);
        dashboardPage = transferPage.transfer(amount, fromCardNumber);
    }

    @Когда("пользователь переводит {int} рублей с карты с номером {string} на свою 1 карту с главной страницы")
    public void transferToFirstCard(int amount, String fromCardNumber) {
        TransferPage transferPage = dashboardPage.clickTransferButton(0); // индекс 0 — первая карта
        dashboardPage = transferPage.transfer(amount, fromCardNumber);
    }

    @Тогда("баланс карты с номером {string} должен стать {int} рублей")
    public void verifyBalance(String cardNumber, int expectedBalance) {
        int actualBalance = dashboardPage.getCardBalance(cardNumber);
        assertEquals(expectedBalance, actualBalance,
                "Баланс карты " + cardNumber + " не соответствует ожидаемому");
    }

    @Тогда("баланс его 1 карты из списка на главной странице должен стать {int} рублей")
    public void verifyFirstCardBalance(int expectedBalance) {
        int actualBalance = dashboardPage.getCardBalance(0);
        assertEquals(expectedBalance, actualBalance,
                "Баланс первой карты не соответствует ожидаемому");
    }
}