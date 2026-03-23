package ru.netology.ibank.steps;

import com.codeborne.selenide.Configuration;
import io.cucumber.java.ru.Когда;
import io.cucumber.java.ru.Пусть;
import io.cucumber.java.ru.Тогда;
import ru.netology.ibank.page.DashboardPage;
import ru.netology.ibank.page.LoginPage;
import ru.netology.ibank.page.TransferPage;
import ru.netology.ibank.page.VerificationPage;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.webdriver;
import static com.codeborne.selenide.WebDriverConditions.url;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TransferSteps {
    private static final String SUT_URL = "http://localhost:9999";
    private static final String VERIFICATION_CODE = "12345";

    private DashboardPage dashboardPage;

    @Пусть("пользователь залогинен с именем {string} и паролем {string}")
    public void login(String login, String password) {
        Configuration.headless = Boolean.parseBoolean(System.getProperty("selenide.headless", "false"));

        open(SUT_URL);
        LoginPage loginPage = new LoginPage();
        VerificationPage verificationPage = loginPage.validLogin(login, password);
        dashboardPage = verificationPage.validVerify(VERIFICATION_CODE);
        webdriver().shouldHave(url(SUT_URL + "/dashboard"));
    }

    @Когда("пользователь переводит {int} рублей с карты с номером {string} на свою 1 карту с главной страницы")
    public void transfer(int amount, String fromCardNumber) {
        // Нажимаем "Пополнить" на первой карте (получатель)
        TransferPage transferPage = dashboardPage.clickTransferButton(0);
        // Указываем карту-отправитель
        dashboardPage = transferPage.transfer(amount, fromCardNumber);
    }

    @Тогда("баланс его 1 карты из списка на главной странице должен стать {int} рублей")
    public void verifyBalance(int expectedBalance) {
        int actualBalance = dashboardPage.getCardBalance(0);
        assertEquals(expectedBalance, actualBalance,
                "Баланс первой карты не соответствует ожидаемому");
    }
}