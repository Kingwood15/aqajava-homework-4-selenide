package ru.netology.domain;

import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.*;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class cardDeliveryTest {

    @BeforeEach
    void openBrowser() {
        open("http://localhost:9999");
    }

    public String setDate(int shiftDay) {
        LocalDate date = LocalDate.now();
        String testDate;

        // изменение даты minus[Days][weeks][months][years](int n): отнимает от даты некоторое количество дней,
        // plus[Days][weeks][months][years](int n): добавляет к дате некоторое количество дней
        // сдиг даты относительно сегоднешнего дня
        if (shiftDay != 0) {
            date = date.plusDays(shiftDay);
        }
        // контроль за временем
        System.out.println("Trace of time: " + date);
        // переворачиваем написание даты
        int day = date.getDayOfMonth();
        int month = date.getMonthValue();
        int year = date.getYear();
        // добавляем 0, если день и/или месяц состоят из одного числа
        if (day < 10) {
            if (month < 10) {
                testDate = String.join(".", "0" + Integer.toString(day), "0" + Integer.toString(month), Integer.toString(year));
            } else {
                testDate = String.join(".", "0" + Integer.toString(day), Integer.toString(month), Integer.toString(year));
            }
        } else {
            if (month < 10) {
                testDate = String.join(".", Integer.toString(day), "0" + Integer.toString(month), Integer.toString(year));
            } else {
                testDate = String.join(".", Integer.toString(day), Integer.toString(month), Integer.toString(year));
            }
        }
        // контроль за временем
        System.out.println("Trace of time: " + testDate);
        // делаем поле даты доступной для записи
        $(".form").$("[data-test-id = 'date'] input").doubleClick();
        $("[data-test-id=date] input").sendKeys("BackSpace");

        return testDate;
    }

    @Test
    void shouldTestOrderPositive() {
        SelenideElement form = $(".form");

        form.$("[data-test-id = 'city'] input").setValue("Улан-Удэ");
        form.$("[data-test-id = 'date'] input").setValue(setDate(3));
        form.$("[data-test-id = 'name'] input").setValue("Иванова Анна");
        form.$("[data-test-id = 'phone'] input").setValue("+71234567890");
        form.$("[data-test-id = 'agreement']").click();
        form.$$("[type = 'button']").last().click();

        $("[data-test-id='notification']").shouldBe(visible, Duration.ofSeconds(15));
    }

    @Test
    void shouldTestOrderCityNotAdminCenter() {
        SelenideElement form = $(".form");

        form.$("[data-test-id = 'city'] input").setValue("Стерлитамак");
        form.$("[data-test-id = 'date'] input").setValue(setDate(3));
        form.$("[data-test-id = 'name'] input").setValue("Иванова Анна");
        form.$("[data-test-id = 'phone'] input").setValue("+71234567890");
        form.$("[data-test-id = 'agreement']").click();
        form.$$("[type = 'button']").last().click();

        $("[data-test-id='city'] [class='input__sub']").shouldHave(exactText("Доставка в выбранный город" +
                " недоступна"));
    }

    @Test
    void shouldTestOrderCityLatin() {
        SelenideElement form = $(".form");

        form.$("[data-test-id = 'city'] input").setValue("Kazan");
        form.$("[data-test-id = 'date'] input").setValue(setDate(3));
        form.$("[data-test-id = 'name'] input").setValue("Иванова Анна");
        form.$("[data-test-id = 'phone'] input").setValue("+71234567890");
        form.$("[data-test-id = 'agreement']").click();
        form.$$("[type = 'button']").last().click();

        $("[data-test-id='city'] [class='input__sub']").shouldHave(exactText("Доставка в выбранный город" +
                " недоступна"));
    }

    @Test
    void shouldTestOrderDateOld() {
        SelenideElement form = $(".form");

        form.$("[data-test-id = 'city'] input").setValue("Казань");
        form.$("[data-test-id = 'date'] input").setValue(setDate(-3));
        form.$("[data-test-id = 'name'] input").setValue("Иванова Анна");
        form.$("[data-test-id = 'phone'] input").setValue("+71234567890");
        form.$("[data-test-id = 'agreement']").click();
        form.$$("[type = 'button']").last().click();

        $("[data-test-id='date'] [class='input__sub']").shouldHave(exactText("Заказ на выбранную дату" +
                " невозможен"));
    }

    @Test
    void shouldTestOrderDateActual() {
        SelenideElement form = $(".form");

        form.$("[data-test-id = 'city'] input").setValue("Казань");
        form.$("[data-test-id = 'date'] input").setValue(setDate(0));
        form.$("[data-test-id = 'name'] input").setValue("Иванова Анна");
        form.$("[data-test-id = 'phone'] input").setValue("+71234567890");
        form.$("[data-test-id = 'agreement']").click();
        form.$$("[type = 'button']").last().click();

        $("[data-test-id='date'] [class='input__sub']").shouldHave(exactText("Заказ на выбранную дату" +
                " невозможен"));
    }

    @Test
    void shouldTestOrderDateFuture() {
        SelenideElement form = $(".form");

        form.$("[data-test-id = 'city'] input").setValue("Казань");
        form.$("[data-test-id='date'] [value]").setValue(setDate(150));
        form.$("[data-test-id = 'name'] input").setValue("Иванова Анна");
        form.$("[data-test-id = 'phone'] input").setValue("+71234567890");
        form.$("[data-test-id = 'agreement']").click();
        form.$$("[type = 'button']").last().click();

        $("[data-test-id='notification']").shouldBe(visible, Duration.ofSeconds(15));
    }

    @Test
    void shouldTestOrderNameHyphen() {
        SelenideElement form = $(".form");

        form.$("[data-test-id = 'city'] input").setValue("Казань");
        form.$("[data-test-id = 'date'] input").setValue(setDate(3));
        form.$("[data-test-id = 'name'] input").setValue("Сергеев-Петров Андрей");
        form.$("[data-test-id = 'phone'] input").setValue("+71234567890");
        form.$("[data-test-id = 'agreement']").click();
        form.$$("[type = 'button']").last().click();

        $("[data-test-id='notification']").shouldBe(visible, Duration.ofSeconds(15));
    }

    @Test
    void shouldTestOrderNameLatin() {
        SelenideElement form = $(".form");

        form.$("[data-test-id = 'city'] input").setValue("Казань");
        form.$("[data-test-id = 'date'] input").setValue(setDate(3));
        form.$("[data-test-id = 'name'] input").setValue("Sirotkin Dmitry");
        form.$("[data-test-id = 'phone'] input").setValue("+71234567890");
        form.$("[data-test-id = 'agreement']").click();
        form.$$("[type = 'button']").last().click();

        $("[data-test-id='name'] [class='input__sub']").shouldHave(exactText("Имя и Фамилия указаные неверно." +
                " Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    void shouldTestOrderPhoneLowLength() {
        SelenideElement form = $(".form");

        form.$("[data-test-id = 'city'] input").setValue("Казань");
        form.$("[data-test-id = 'date'] input").setValue(setDate(3));
        form.$("[data-test-id = 'name'] input").setValue("Петров Анатолий");
        form.$("[data-test-id = 'phone'] input").setValue("123");
        form.$("[data-test-id = 'agreement']").click();
        form.$$("[type = 'button']").last().click();

        $("[data-test-id='phone'] [class='input__sub']").shouldHave(exactText("Телефон указан неверно." +
                " Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    void shouldTestOrderPhoneHighLength() {
        SelenideElement form = $(".form");

        form.$("[data-test-id = 'city'] input").setValue("Казань");
        form.$("[data-test-id = 'date'] input").setValue(setDate(3));
        form.$("[data-test-id = 'name'] input").setValue("Петров Анатолий");
        form.$("[data-test-id = 'phone'] input").setValue("+712345678901");
        form.$("[data-test-id = 'agreement']").click();
        form.$$("[type = 'button']").last().click();

        $("[data-test-id='phone'] [class='input__sub']").shouldHave(exactText("Телефон указан неверно." +
                " Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    void shouldTestOrderPhoneText() {
        SelenideElement form = $(".form");

        form.$("[data-test-id = 'city'] input").setValue("Казань");
        form.$("[data-test-id = 'date'] input").setValue(setDate(3));
        form.$("[data-test-id = 'name'] input").setValue("Петров Анатолий");
        form.$("[data-test-id = 'phone'] input").setValue("number");
        form.$("[data-test-id = 'agreement']").click();
        form.$$("[type = 'button']").last().click();

        $("[data-test-id='phone'] [class='input__sub']").shouldHave(exactText("Телефон указан неверно." +
                " Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    void shouldTestOrderCheckboxOff() {
        SelenideElement form = $(".form");

        form.$("[data-test-id = 'city'] input").setValue("Казань");
        form.$("[data-test-id = 'date'] input").setValue(setDate(3));
        form.$("[data-test-id = 'name'] input").setValue("Петров Анатолий");
        form.$("[data-test-id = 'phone'] input").setValue("+71234567890");
        //form.$("[data-test-id = 'agreement']").click();
        form.$$("[type = 'button']").last().click();

        $("[data-test-id='agreement'] [class='checkbox__text']").shouldHave(exactText("Я соглашаюсь с" +
                " условиями обработки и использования моих персональных данных"));
    }
}
