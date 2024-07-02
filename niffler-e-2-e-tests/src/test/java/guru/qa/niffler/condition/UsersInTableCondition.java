package guru.qa.niffler.condition;

import com.codeborne.selenide.CheckResult;
import com.codeborne.selenide.Driver;
import com.codeborne.selenide.WebElementsCondition;
import com.codeborne.selenide.impl.CollectionSource;
import guru.qa.niffler.model.UserJson;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class UsersInTableCondition extends WebElementsCondition {

    private final UserJson[] expectedUsers;


    public UsersInTableCondition(UserJson[] expectedUsers) {
        this.expectedUsers = expectedUsers;
    }

    @Nonnull
    @Override
    public CheckResult check(Driver driver, List<WebElement> elements) {
        if (elements.size() != expectedUsers.length) {
            return CheckResult.rejected(
                    "Users table size mismatch",
                    elements.size()
            );
        }

        for (int i = 0; i < elements.size(); i++) {
            WebElement row = elements.get(i);
            UserJson expectedUserForRow = expectedUsers[i];

            List<WebElement> td = row.findElements(By.cssSelector("td"));

            boolean userNameResult = td.get(1).getText().equals(
                    expectedUserForRow.username()
            );

            if (!userNameResult) {
                return CheckResult.rejected(
                        "User table: username mismatch",
                        td.get(1).getText()
                );
            }

            boolean firstNameResult = td.get(2).getText().equals(
                    expectedUserForRow.firstname()
            );

            if (!firstNameResult) {
                return CheckResult.rejected(
                        "User table: firstname mismatch",
                        td.get(2).getText()
                );
            }

            boolean surNameResult = td.get(3).getText().equals(
                    expectedUserForRow.surname()
            );

            if (!surNameResult) {
                return CheckResult.rejected(
                        "User table: surname mismatch",
                        td.get(3).getText()
                );
            }

            boolean currencyResult = td.get(4).getText().equals(
                    expectedUserForRow.currency()
            );

            if (!currencyResult) {
                return CheckResult.rejected(
                        "User table: currency mismatch",
                        td.get(4).getText()
                );
            }

            boolean photoResult = td.get(4).getText().equals(
                    expectedUserForRow.photo()
            );

            if (!photoResult) {
                return CheckResult.rejected(
                        "User table: photo mismatch",
                        td.get(4).getText()
                );
            }

            boolean photoSmallResult = td.get(5).getText().equals(
                    expectedUserForRow.photoSmall()
            );

            if (!photoSmallResult) {
                return CheckResult.rejected(
                        "User table: photoSmall mismatch",
                        td.get(5).getText()
                );
            }

            boolean friendStateResult = td.get(6).getText().equals(
                    expectedUserForRow.friendState()
            );

            if (!friendStateResult) {
                return CheckResult.rejected(
                        "User table: friendState mismatch",
                        td.get(6).getText()
                );
            }
        }

        return CheckResult.accepted();
    }

    @Override
    public void fail(CollectionSource collection, CheckResult lastCheckResult, @Nullable Exception cause, long timeoutMs) {
        StringBuilder actualUsers = new StringBuilder();
        StringBuilder expectedUsersSB = new StringBuilder();
        List<WebElement> rows = collection.getElements();

        for (WebElement row : rows) {
            List<WebElement> cells = row.findElements(By.cssSelector("td"));

            String formattedRow = String.format(" - %s | %s | %s| %s | %s | %s | %s\n",
                    cells.get(1).getText(),
                    cells.get(2).getText(),
                    cells.get(3).getText(),
                    cells.get(4).getText(),
                    cells.get(5).getText(),
                    cells.get(6).getText(),
                    cells.get(7).getText()
            );

            actualUsers.append(formattedRow);
        }

        for (UserJson user : expectedUsers) {
            String formattedExpected = String.format(" - %s | %s | %s| %s | %s | %s | %s\n",
                    user.username(),
                    user.firstname(),
                    user.surname(),
                    user.currency(),
                    user.photo(),
                    user.photoSmall(),
                    user.friendState()
            );

            expectedUsersSB.append(formattedExpected);
        }

        String actualUsersText = actualUsers.toString();
        String expectedUsersText = expectedUsersSB.toString();
        String message = lastCheckResult.getMessageOrElse(() -> "Users mismatch");

        throw new UserMismatchException(message, collection, expectedUsersText, actualUsersText, explanation, timeoutMs, cause);
    }

    @Override
    public String toString() {
        return null;
    }
}
