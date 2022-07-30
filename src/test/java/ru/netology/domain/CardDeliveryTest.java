package ru.netology.domain;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.*;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class CardDeliveryTest {

    @BeforeEach
    void openBrowser() {
        open("http://localhost:9999");
    }

    public String generateDate(int days) {
        return LocalDate.now().plusDays(days).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    @Test
    void shouldTestOrderAdminCenter() {
        SelenideElement form = $(".form");
        String planningDate = generateDate(3);

        form.$("[data-test-id = 'city'] input").setValue("Уфа");
        form.$("[data-test-id = 'date'] input").doubleClick();
        $("[data-test-id=date] input").sendKeys(Keys.BACK_SPACE);
        form.$("[data-test-id = 'date'] input").setValue(planningDate);
        form.$("[data-test-id = 'name'] input").setValue("Иванова Анна");
        form.$("[data-test-id = 'phone'] input").setValue("+71234567890");
        form.$("[data-test-id = 'agreement']").click();
        form.$$("button.button").last().click();

        $(".notification__content")
                .shouldHave(Condition.text("Встреча успешно забронирована на " + planningDate), Duration.ofSeconds(15))
                .shouldBe(Condition.visible);
    }

    @Test
    void shouldTestOrderAdminCenterHyphenated() {
        SelenideElement form = $(".form");
        String planningDate = generateDate(3);

        form.$("[data-test-id = 'city'] input").setValue("Улан-Удэ");
        form.$("[data-test-id = 'date'] input").doubleClick();
        $("[data-test-id=date] input").sendKeys(Keys.BACK_SPACE);
        form.$("[data-test-id = 'date'] input").setValue(planningDate);
        form.$("[data-test-id = 'name'] input").setValue("Иванова Анна");
        form.$("[data-test-id = 'phone'] input").setValue("+71234567890");
        form.$("[data-test-id = 'agreement']").click();
        form.$$("button.button").last().click();

        $(".notification__content")
                .shouldHave(Condition.text("Встреча успешно забронирована на " + planningDate), Duration.ofSeconds(15))
                .shouldBe(Condition.visible);
    }

    @Test
    void shouldTestOrderCityNotAdminCenter() {
        SelenideElement form = $(".form");
        String planningDate = generateDate(3);

        form.$("[data-test-id = 'city'] input").setValue("Стерлитамак");
        form.$("[data-test-id = 'date'] input").doubleClick();
        $("[data-test-id=date] input").sendKeys(Keys.BACK_SPACE);
        form.$("[data-test-id = 'date'] input").setValue(planningDate);
        form.$("[data-test-id = 'name'] input").setValue("Иванова Анна");
        form.$("[data-test-id = 'phone'] input").setValue("+71234567890");
        form.$("[data-test-id = 'agreement']").click();
        form.$$("button.button").last().click();

        $("[data-test-id='city'] [class='input__sub']").shouldHave(exactText("Доставка в выбранный город" +
                " недоступна"));
    }

    @Test
    void shouldTestOrderCityLatin() {
        SelenideElement form = $(".form");
        String planningDate = generateDate(3);

        form.$("[data-test-id = 'city'] input").setValue("Kazan");
        form.$("[data-test-id = 'date'] input").doubleClick();
        $("[data-test-id=date] input").sendKeys(Keys.BACK_SPACE);
        form.$("[data-test-id = 'date'] input").setValue(planningDate);
        form.$("[data-test-id = 'name'] input").setValue("Иванова Анна");
        form.$("[data-test-id = 'phone'] input").setValue("+71234567890");
        form.$("[data-test-id = 'agreement']").click();
        form.$$("button.button").last().click();

        $("[data-test-id='city'] [class='input__sub']").shouldHave(exactText("Доставка в выбранный город" +
                " недоступна"));
    }

    @Test
    void shouldTestOrderDateOld() {
        SelenideElement form = $(".form");
        String planningDate = generateDate(-3);

        form.$("[data-test-id = 'city'] input").setValue("Казань");
        form.$("[data-test-id = 'date'] input").doubleClick();
        $("[data-test-id=date] input").sendKeys(Keys.BACK_SPACE);
        form.$("[data-test-id = 'date'] input").setValue(planningDate);
        form.$("[data-test-id = 'name'] input").setValue("Иванова Анна");
        form.$("[data-test-id = 'phone'] input").setValue("+71234567890");
        form.$("[data-test-id = 'agreement']").click();
        form.$$("button.button").last().click();

        $("[data-test-id='date'] [class='input__sub']").shouldHave(exactText("Заказ на выбранную дату" +
                " невозможен"));
    }

    @Test
    void shouldTestOrderDateActual() {
        SelenideElement form = $(".form");
        String planningDate = generateDate(0);

        form.$("[data-test-id = 'city'] input").setValue("Казань");
        form.$("[data-test-id = 'date'] input").doubleClick();
        $("[data-test-id=date] input").sendKeys(Keys.BACK_SPACE);
        form.$("[data-test-id = 'date'] input").setValue(planningDate);
        form.$("[data-test-id = 'name'] input").setValue("Иванова Анна");
        form.$("[data-test-id = 'phone'] input").setValue("+71234567890");
        form.$("[data-test-id = 'agreement']").click();
        form.$$("button.button").last().click();

        $("[data-test-id='date'] [class='input__sub']").shouldHave(exactText("Заказ на выбранную дату" +
                " невозможен"));
    }

    @Test
    void shouldTestOrderDateFuture() {
        SelenideElement form = $(".form");
        String planningDate = generateDate(150);

        form.$("[data-test-id = 'city'] input").setValue("Казань");
        form.$("[data-test-id = 'date'] input").doubleClick();
        $("[data-test-id=date] input").sendKeys(Keys.BACK_SPACE);
        form.$("[data-test-id='date'] [value]").setValue(planningDate);
        form.$("[data-test-id = 'name'] input").setValue("Иванова Анна");
        form.$("[data-test-id = 'phone'] input").setValue("+71234567890");
        form.$("[data-test-id = 'agreement']").click();
        form.$$("button.button").last().click();

        $(".notification__content")
                .shouldHave(Condition.text("Встреча успешно забронирована на " + planningDate), Duration.ofSeconds(15))
                .shouldBe(Condition.visible);
    }

    @Test
    void shouldTestOrderNameHyphen() {
        SelenideElement form = $(".form");
        String planningDate = generateDate(3);

        form.$("[data-test-id = 'city'] input").setValue("Казань");
        form.$("[data-test-id = 'date'] input").doubleClick();
        $("[data-test-id=date] input").sendKeys(Keys.BACK_SPACE);
        form.$("[data-test-id = 'date'] input").setValue(planningDate);
        form.$("[data-test-id = 'name'] input").setValue("Сергеев-Петров Андрей");
        form.$("[data-test-id = 'phone'] input").setValue("+71234567890");
        form.$("[data-test-id = 'agreement']").click();
        form.$$("button.button").last().click();

        $(".notification__content")
                .shouldHave(Condition.text("Встреча успешно забронирована на " + planningDate), Duration.ofSeconds(15))
                .shouldBe(Condition.visible);
    }

    @Test
    void shouldTestOrderNameLatin() {
        SelenideElement form = $(".form");
        String planningDate = generateDate(3);

        form.$("[data-test-id = 'city'] input").setValue("Казань");
        form.$("[data-test-id = 'date'] input").doubleClick();
        $("[data-test-id=date] input").sendKeys(Keys.BACK_SPACE);
        form.$("[data-test-id = 'date'] input").setValue(planningDate);
        form.$("[data-test-id = 'name'] input").setValue("Sirotkin Dmitry");
        form.$("[data-test-id = 'phone'] input").setValue("+71234567890");
        form.$("[data-test-id = 'agreement']").click();
        form.$$("button.button").last().click();

        $("[data-test-id='name'] [class='input__sub']").shouldHave(exactText("Имя и Фамилия указаные неверно." +
                " Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    void shouldTestOrderPhoneLowLength() {
        SelenideElement form = $(".form");
        String planningDate = generateDate(3);

        form.$("[data-test-id = 'city'] input").setValue("Казань");
        form.$("[data-test-id = 'date'] input").doubleClick();
        $("[data-test-id=date] input").sendKeys(Keys.BACK_SPACE);
        form.$("[data-test-id = 'date'] input").setValue(planningDate);
        form.$("[data-test-id = 'name'] input").setValue("Петров Анатолий");
        form.$("[data-test-id = 'phone'] input").setValue("123");
        form.$("[data-test-id = 'agreement']").click();
        form.$$("button.button").last().click();

        $("[data-test-id='phone'] [class='input__sub']").shouldHave(exactText("Телефон указан неверно." +
                " Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    void shouldTestOrderPhoneHighLength() {
        SelenideElement form = $(".form");
        String planningDate = generateDate(3);

        form.$("[data-test-id = 'city'] input").setValue("Казань");
        form.$("[data-test-id = 'date'] input").doubleClick();
        $("[data-test-id=date] input").sendKeys(Keys.BACK_SPACE);
        form.$("[data-test-id = 'date'] input").setValue(planningDate);
        form.$("[data-test-id = 'name'] input").setValue("Петров Анатолий");
        form.$("[data-test-id = 'phone'] input").setValue("+712345678901");
        form.$("[data-test-id = 'agreement']").click();
        form.$$("button.button").last().click();

        $("[data-test-id='phone'] [class='input__sub']").shouldHave(exactText("Телефон указан неверно." +
                " Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    void shouldTestOrderPhoneText() {
        SelenideElement form = $(".form");
        String planningDate = generateDate(3);

        form.$("[data-test-id = 'city'] input").setValue("Казань");
        form.$("[data-test-id = 'date'] input").doubleClick();
        $("[data-test-id=date] input").sendKeys(Keys.BACK_SPACE);
        form.$("[data-test-id = 'date'] input").setValue(planningDate);
        form.$("[data-test-id = 'name'] input").setValue("Петров Анатолий");
        form.$("[data-test-id = 'phone'] input").setValue("number");
        form.$("[data-test-id = 'agreement']").click();
        form.$$("button.button").last().click();

        $("[data-test-id='phone'] [class='input__sub']").shouldHave(exactText("Телефон указан неверно." +
                " Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    void shouldTestOrderCheckboxOff() {
        SelenideElement form = $(".form");
        String planningDate = generateDate(3);

        form.$("[data-test-id = 'city'] input").setValue("Казань");
        form.$("[data-test-id = 'date'] input").doubleClick();
        $("[data-test-id=date] input").sendKeys(Keys.BACK_SPACE);
        form.$("[data-test-id = 'date'] input").setValue(planningDate);
        form.$("[data-test-id = 'name'] input").setValue("Петров Анатолий");
        form.$("[data-test-id = 'phone'] input").setValue("+71234567890");
        form.$$("button.button").last().click();

        $("[data-test-id='agreement'] [class='checkbox__text']").shouldHave(exactText("Я соглашаюсь с" +
                " условиями обработки и использования моих персональных данных"));
    }
}
