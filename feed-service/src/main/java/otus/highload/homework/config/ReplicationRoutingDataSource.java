package otus.highload.homework.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Log4j2
@RequiredArgsConstructor
public class ReplicationRoutingDataSource extends AbstractRoutingDataSource {

    private final int readReplicasCount;
    private int current = 0;

    @Override
    protected Object determineCurrentLookupKey() {
        if (TransactionSynchronizationManager.isCurrentTransactionReadOnly()) {
            current++;
            current %= readReplicasCount;
            var dataSourceType = "read-" + current;
            //log.info("current dataSourceType : {}", dataSourceType);
            return dataSourceType;
        } else {
            //log.info("current dataSourceType : write");
            return "write";
        }

    }
}
