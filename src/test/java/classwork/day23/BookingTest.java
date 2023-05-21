package classwork.day23;

import com.codeborne.selenide.WebDriverRunner;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class BookingTest {

    @Test
    public void pricePerNightTest() {
        SimpleDateFormat needFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar myCalendar = Calendar.getInstance();
        myCalendar.setTime(new Date());
        myCalendar.add(Calendar.DAY_OF_MONTH, 3);
        open("https://booking.com");
        $x("//div[@role='dialog'][@aria-modal='true']//button").click();
        $("[name='ss']").sendKeys("Париж");
        $x("//*[@data-testid='autocomplete-results']/li[1]/descendant:: div[contains(text(),'Париж')]").click();
        $("[data-date='" + needFormat.format(myCalendar.getTime()) + "']").click();
        myCalendar.add(Calendar.DAY_OF_MONTH, 7);
        $("[data-date='" + needFormat.format(myCalendar.getTime()) + "']").click();
        $("[data-testid='occupancy-config']").click();
        $x("//input[@id='group_adults']/following-sibling::div/button[2]").click();
        $x("//input[@id='group_adults']/following-sibling::div/button[2]").click();
        $x("//input[@id='no_rooms']/following-sibling::div/button[2]").click();
        $x("//button[@type='submit']").click();
        try {
            $x("//*[@id='filter_group_pri_:Rcq:']//descendant::div[@data-filters-item='pri:pri=5']//descendant::span[2]").click();
        } catch (NoSuchElementException e) {
            Actions make = new Actions(WebDriverRunner.getWebDriver());
            make
                    .clickAndHold($x("//*[@id='filter_group_price_:Rcq:']//descendant::div[@data-testid='filters-group-histogram']//following-sibling::div/div/div[1]"))
                    .moveByOffset(200, -25)
                    .release()
                    .build()
                    .perform();
        }
        $("[data-testid='overlay-spinner']").shouldNotBe(visible);
        $("[data-testid='sorters-dropdown-trigger']").click();
        $("[data-id='price']").click();
        try {
            Assert.assertTrue(Integer.parseInt($x("//*[@data-block-id='hotel_list']//descendant::div[@data-testid='property-card'][1]//descendant::span[@data-testid='price-and-discounted-price']").getText().replaceAll("[^0-9]", "")) / 7 >=
                    Integer.parseInt($x("//*[@id='filter_group_pri_:Rcq:']//descendant::div[@data-filters-item='pri:pri=5']//descendant::div[@data-testid='filters-group-label-content']").getText().replaceAll("[^0-9]", ""))
            );
        } catch (NoSuchElementException e) {
            Assert.assertTrue(Integer.parseInt($x("//*[@data-block-id='hotel_list']//descendant::div[@data-testid='property-card'][1]//descendant::span[@data-testid='price-and-discounted-price']").getText().replaceAll("[^0-9]", "")) / 7 >=
                    Integer.parseInt(Objects.requireNonNull($x("//*[@id='filter_group_price_:Rcq:']//descendant::div[@data-testid='filters-group-histogram']//following-sibling::div/div/input[1]").getAttribute("value")))
            );
        }
    }


}
