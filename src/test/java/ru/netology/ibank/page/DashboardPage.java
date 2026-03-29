package ru.netology.ibank.page;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class DashboardPage {

    private final ElementsCollection cards = $$(".list__item");

    public DashboardPage() {
        cards.shouldHave(CollectionCondition.sizeGreaterThan(0));
    }


    public int getCardBalance(int index) {
        String text = cards.get(index).text();
        return extractBalance(text);
    }


    public TransferPage clickTransferButton(int index) {
        SelenideElement cardElement = cards.get(index);
        cardElement.$("[data-test-id='action-deposit']").click();
        return new TransferPage();
    }


    public int getCardsCount() {
        return cards.size();
    }


    public String getCardText(int index) {
        return cards.get(index).text();
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