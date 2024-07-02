package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.data.entity.SpendEntity;
import guru.qa.niffler.data.repository.SpendRepository;
import guru.qa.niffler.model.SpendJson;

public class SpendJdbcExtension extends AbstractSpendExtension {

    private final SpendRepository spendRepository = SpendRepository.getInstance();

    @Override
    protected SpendJson createSpend(SpendJson spendJson) {
        return SpendJson.fromEntity(spendRepository.createSpend(SpendEntity.fromJson(spendJson)));
    }

    @Override
    protected void removeSpend(SpendJson spend) {
        spendRepository.removeSpend(SpendEntity.fromJson(spend));
    }
}
