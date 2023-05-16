package ru.netology;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.open;


class CardDelivery {

    private String generateDate(int addDays, String pattern) {
        return LocalDate.now().plusDays(addDays).format(DateTimeFormatter.ofPattern(pattern));
    }

    @Test
    void shouldRegisterByManualTab() {
        open("http://localhost:9999/");
        $("[data-test-id=city] input").setValue("Санкт-Петербург");
//        $("[data-test-id=date] input").sendKeys(Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE + "18.05.2023");
        $("[data-test-id=date] input").sendKeys(Keys.CONTROL, Keys.BACK_SPACE);
        String planingDate = generateDate(5, "dd.MM.yyyy");
        $("[data-test-id=date] input").sendKeys(planingDate);
        $("[data-test-id=name] input").setValue("Иванов Иван");
        $("[data-test-id=phone] input").setValue("+79999999999");
        $("[data-test-id=agreement]").click();
        $(By.className("button")).click();

        $("[data-test-id=notification] .notification__content")
                .shouldBe(Condition.visible,
                        Duration.ofSeconds(15))
                .shouldHave(Condition.text("Встреча успешно забронирована на " + planingDate));
    }

    @Test
    void shouldRegisterByChoosingTab() {
        open("http://localhost:9999/");
        $("[data-test-id=city] input").setValue("Са");
        $x("//span[text() = 'Санкт-Петербург']").click();
        $("[data-test-id=date] input").click();
        String planingDate = generateDate(7, "dd.MM.yyyy");
        String currentMonth = generateDate(0, "MM");
        String planingMonth = generateDate(7, "MM");
        String planingDay = generateDate(7,"d");
        if (!currentMonth.equals(planingMonth)) {
            $("[data-step='1']").click();
        }
        $$("[data-day]").find(Condition.text(planingDay)).click();
        $("[data-test-id=name] input").setValue("Иванов Иван");
        $("[data-test-id=phone] input").setValue("+79999999999");
        $("[data-test-id=agreement]").click();
        $(By.className("button")).click();

        $("[data-test-id=notification] .notification__content")
                .shouldBe(Condition.visible,
                        Duration.ofSeconds(15))
                .shouldHave(Condition.text("Встреча успешно забронирована на " + planingDate));
    }
}
