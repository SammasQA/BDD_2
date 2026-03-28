package ru.netology.ibank.page;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class DashboardPage {

    private ElementsCollection cards = $$(".list__item");

    public DashboardPage() {

        cards.shouldHave(CollectionCondition.sizeGreaterThan(0));
    }

    public int getCardBalance(int index) {
        String text = cards.get(index).text();
        return extractBalance(text);
    }

    public int getCardBalance(String cardNumber) {
        int index = getCardIndexByNumber(cardNumber);
        return getCardBalance(index);
    }

    public TransferPage clickTransferButton(int index) {
        SelenideElement cardElement = cards.get(index);
        cardElement.$("[data-test-id='action-deposit']").click();
        return new TransferPage();
    }

    public TransferPage clickTransferButton(String cardNumber) {
        int index = getCardIndexByNumber(cardNumber);
        return clickTransferButton(index);
    }

    private int getCardIndexByNumber(String cardNumber) {

        String cleaned = cardNumber.replaceAll("\\s+", "");
        String lastFour = cleaned.length() > 4 ? cleaned.substring(cleaned.length() - 4) : cleaned;

        for (int i = 0; i < cards.size(); i++) {
            String text = cards.get(i).text();
            if (text.contains(lastFour)) {
                return i;
            }
        }
        throw new IllegalArgumentException("Карта с номером, оканчивающимся на " + lastFour + ", не найдена");
    }

    private int extractBalance(String text) {
        String balanceStart = "баланс: ";
        String balanceFinish = " р.";
        int start = text.indexOf(balanceStart);
        int finish = text.indexOf(balanceFinish);
        if (start == -1 || finish == -1) {
            throw new IllegalStateException("Не удалось найти баланс в тексте: " + text);
        }
        String balanceStr = text.substring(start + balanceStart.length(), finish).trim();
        return Integer.parseInt(balanceStr);
    }
}