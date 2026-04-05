package ru.netology.ibank.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$$;

public class DashboardPage {

    private final ElementsCollection cards = $$(".list__item");
    private final String balanceStart = "баланс: ";
    private final String balanceFinish = " р.";

    public DashboardPage() {
        cards.first().shouldBe(visible);
    }


    public String getCardMaskedNumber(int index) {
        SelenideElement card = cards.get(index);
        return extractMaskedNumber(card.getText());
    }


    public int getCardBalance(int index) {
        SelenideElement card = cards.get(index);
        return extractBalance(card.getText());
    }


    public TransferPage clickTransferButton(int index) {
        SelenideElement card = cards.get(index);
        card.$("[data-test-id='action-deposit']").click();
        return new TransferPage();
    }


    public void verifyCardBalance(int index, int expectedBalance) {
        SelenideElement card = cards.get(index);
        card.shouldBe(visible);
        String expectedText = balanceStart + expectedBalance + balanceFinish;
        card.shouldHave(text(expectedText));
    }


    public String getFirstCardMaskedNumber() {
        return getCardMaskedNumber(0);
    }


    private SelenideElement getCardByMaskedNumber(String maskedNumber) {
        return cards.findBy(text(maskedNumber));
    }


    public int getCardBalance(String maskedNumber) {
        SelenideElement card = getCardByMaskedNumber(maskedNumber);
        return extractBalance(card.getText());
    }


    public TransferPage clickTransferButton(String receiverMaskedNumber) {
        SelenideElement card = getCardByMaskedNumber(receiverMaskedNumber);
        card.$("[data-test-id='action-deposit']").click();
        return new TransferPage();
    }

    public void verifyCardBalance(String maskedNumber, int expectedBalance) {
        SelenideElement card = getCardByMaskedNumber(maskedNumber);
        card.shouldBe(visible);
        String expectedText = balanceStart + expectedBalance + balanceFinish;
        card.shouldHave(text(expectedText));
    }



    private String extractMaskedNumber(String cardText) {
        // Ищем номер до запятой (перед словом "баланс")
        int commaIndex = cardText.indexOf(',');
        if (commaIndex != -1) {
            return cardText.substring(0, commaIndex).trim();
        }
        // Регулярное выражение для формата "XXXX XXXX XXXX XXXX"
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("([\\d*]{4} ){3}[\\d*]{4}");
        java.util.regex.Matcher matcher = pattern.matcher(cardText);
        if (matcher.find()) {
            return matcher.group();
        }
        throw new IllegalStateException("Не удалось извлечь номер карты из текста: " + cardText);
    }

    private int extractBalance(String text) {
        int start = text.indexOf(balanceStart);
        int finish = text.indexOf(balanceFinish);
        if (start == -1 || finish == -1) {
            throw new IllegalStateException("Не удалось найти баланс в тексте: " + text);
        }
        String balanceStr = text.substring(start + balanceStart.length(), finish).trim();
        return Integer.parseInt(balanceStr);
    }
}