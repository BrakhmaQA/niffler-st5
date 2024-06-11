package guru.qa.niffler.pages;

import com.codeborne.selenide.SelenideElement;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.codeborne.selenide.Selenide.$;

public class ReactCalendar extends BaseComponent<ReactCalendar> {
    private final SelenideElement calendar = self.$(".react-datepicker__input-container input");

    protected ReactCalendar(SelenideElement self) {
        super(self);
    }

    protected ReactCalendar() {
        super($(".calendar-wrapper"));
    }

    public ReactCalendar setData(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = dateFormat.format(date);

        calendar.setValue(formattedDate);

        return this;
    }

    public ReactCalendar setDataByJsExecutor(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = dateFormat.format(date);

        jsExecutor.executeScript(String.format("document.getElementsByClassName(\".react-datepicker__input-container input\").setDate(\"%s\");", formattedDate));

        return this;
    }
}
