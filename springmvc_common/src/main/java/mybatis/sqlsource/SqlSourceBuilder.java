package mybatis.sqlsource;

import org.apache.ibatis.mapping.SqlSource;

public interface SqlSourceBuilder {

    SqlSource buildCount(SqlSource original);

    SqlSource buildPage(SqlSource original);

}
