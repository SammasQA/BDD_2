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

    @Когда("пользователь переводит {int} рублей с карты с номером {string} на свою {int} карту с главной страницы")
    public void transferToCard(int amount, String fromCardNumber, int toCardIndex) {
        int index = toCardIndex - 1; // пользователь видит карты с 1, в коллекции индексы с 0
        TransferPage transferPage = dashboardPage.clickTransferButton(index);
        dashboardPage = transferPage.transfer(amount, fromCardNumber);
    }

    @Тогда("баланс его {int} карты из списка на главной странице должен стать {int} рублей")
    public void verifyCardBalance(int cardIndex, int expectedBalance) {
        int index = cardIndex - 1;
        dashboardPage.verifyCardBalance(index, expectedBalance);
    }
}