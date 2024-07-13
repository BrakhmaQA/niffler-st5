package guru.qa.niffler.data.repository.logging;

import com.p6spy.engine.common.StatementInformation;
import com.p6spy.engine.logging.LoggingEventListener;

import java.sql.SQLException;

public class DontLogExecuteBatchEventListener extends LoggingEventListener {
    @Override
    public void onAfterExecuteBatch(StatementInformation statementInformation, long timeElapsedNanos, int[] updateCounts, SQLException e) {
        super.onAfterExecuteBatch(statementInformation, timeElapsedNanos, updateCounts, e);
    }
}
