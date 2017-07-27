package mybatis.spring.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class DynamicDataSource extends AbstractRoutingDataSource {

    private static final ThreadLocal<String> dataSourceHolder = new ThreadLocal<>();

    public static void setDataSourceKey(String dataSourceKey) {
        dataSourceHolder.set(dataSourceKey);
    }

    public static String removeDataSourceKey() {
        String dataSourceKey = dataSourceHolder.get();
        dataSourceHolder.remove();
        return dataSourceKey;
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return dataSourceHolder.get();
    }

}
