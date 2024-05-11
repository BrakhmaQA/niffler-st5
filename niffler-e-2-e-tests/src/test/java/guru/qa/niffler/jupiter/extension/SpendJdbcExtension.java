package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.data.entity.SpendEntity;
import guru.qa.niffler.data.repository.SpendRepository;
import guru.qa.niffler.jupiter.annotation.Spend;
import guru.qa.niffler.model.SpendJson;

import java.util.Date;

public class SpendJdbcExtension extends AbstractSpendExtension {

    private final SpendRepository spendRepository = SpendRepository.getInstance();

    @Override
    protected SpendJson createSpend(Spend spend) {
        SpendJson spendJson = new SpendJson(
                null,
                new Date(),
                spend.category(),
                spend.currency(),
                spend.amount(),
                spend.description(),
                spend.username()
        );

        return spendJson;
    }

    @Override
    protected void removeSpend(SpendJson spend) {
        SpendJson spendJson = new SpendJson(spend.id(), spend.spendDate(), spend.category(),
                spend.currency(), spend.amount(), spend.description(), spend.username());
        spendRepository.removeSpend(SpendEntity.fromJson(spendJson));
    }
}
