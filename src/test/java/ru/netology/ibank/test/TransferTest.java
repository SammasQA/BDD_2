package ru.netology.ibank.test;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.*;
import ru.netology.ibank.page.*;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.webdriver;
import static com.codeborne.selenide.WebDriverConditions.url;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TransferTest {
    private static final String SUT_URL = "http://localhost:9999";
    private static final String LOGIN = "vasya";
    private static final String PASSWORD = "qwerty123";
    private static final String CODE = "12345";
    private static final String CARD1_NUMBER = "5559 0000 0000 0001";
    private static final String CARD2_NUMBER = "5559 0000 0000 0002";

    @BeforeEach
    void loginAndGoToDashboard() {
        Configuration.browser = "chrome";
        Configuration.headless = Boolean.parseBoolean(System.getProperty("selenide.headless", "false"));

        open(SUT_URL);
        LoginPage loginPage = new LoginPage();
        VerificationPage verificationPage = loginPage.validLogin(LOGIN, PASSWORD);
        DashboardPage dashboardPage = verificationPage.validVerify(CODE);
        webdriver().shouldHave(url(SUT_URL + "/dashboard"));
    }

    @Test
    void shouldTransferMoneyBetweenOwnCards() {
        DashboardPage dashboardPage = new DashboardPage();

        int balanceCard1Before = dashboardPage.getCardBalance(0); // отправитель (первая карта)
        int balanceCard2Before = dashboardPage.getCardBalance(1); // получатель (вторая карта)

        int transferAmount = 3000;


        TransferPage transferPage = dashboardPage.clickTransferButton(1);

        dashboardPage = transferPage.transfer(transferAmount, CARD1_NUMBER);

        int balanceCard1After = dashboardPage.getCardBalance(0);
        int balanceCard2After = dashboardPage.getCardBalance(1);

        assertEquals(balanceCard1Before - transferAmount, balanceCard1After,
                "Баланс карты-отправителя должен уменьшиться");
        assertEquals(balanceCard2Before + transferAmount, balanceCard2After,
                "Баланс карты-получателя должен увеличиться");
    }
}