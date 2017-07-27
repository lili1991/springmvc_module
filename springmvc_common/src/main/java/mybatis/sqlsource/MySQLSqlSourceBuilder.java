package mybatis.sqlsource;

import org.apache.ibatis.builder.StaticSqlSource;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.scripting.defaults.RawSqlSource;
import org.apache.ibatis.scripting.xmltags.*;
import org.apache.ibatis.session.Configuration;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MySQLSqlSourceBuilder implements SqlSourceBuilder {

    private static final Field sqlSourceFieldInRawSqlSource = ReflectionUtils.findField(RawSqlSource.class, "sqlSource");
    private static final Field sqlFieldInStaticSqlSource = ReflectionUtils.findField(StaticSqlSource.class, "sql");
    private static final Field configurationFieldInStaticSqlSource = ReflectionUtils.findField(StaticSqlSource.class, "configuration");
    private static final Field parameterMappingsFieldInStaticSqlSource = ReflectionUtils.findField(StaticSqlSource.class, "parameterMappings");
    private static final Field configurationFieldInDynamicSqlSource = ReflectionUtils.findField(DynamicSqlSource.class, "configuration");
    private static final Field rootSqlNodeFieldInDynamicSqlSource = ReflectionUtils.findField(DynamicSqlSource.class, "rootSqlNode");
    private static final Field contentsFieldInMixedSqlNode = ReflectionUtils.findField(MixedSqlNode.class, "contents");
    private static final Field textFieldInTextSqlNode = ReflectionUtils.findField(TextSqlNode.class, "text");
    private static final Field textFieldInStaticTextSqlNode = ReflectionUtils.findField(StaticTextSqlNode.class, "text");

    static {
        ReflectionUtils.makeAccessible(sqlSourceFieldInRawSqlSource);
        ReflectionUtils.makeAccessible(sqlFieldInStaticSqlSource);
        ReflectionUtils.makeAccessible(configurationFieldInStaticSqlSource);
        ReflectionUtils.makeAccessible(parameterMappingsFieldInStaticSqlSource);
        ReflectionUtils.makeAccessible(configurationFieldInDynamicSqlSource);
        ReflectionUtils.makeAccessible(rootSqlNodeFieldInDynamicSqlSource);
        ReflectionUtils.makeAccessible(contentsFieldInMixedSqlNode);
        ReflectionUtils.makeAccessible(textFieldInTextSqlNode);
        ReflectionUtils.makeAccessible(textFieldInStaticTextSqlNode);
    }

    @Override
    public SqlSource buildCount(SqlSource original) {
        if(original instanceof RawSqlSource) {
            SqlSource sqlSource = (SqlSource) ReflectionUtils.getField(sqlSourceFieldInRawSqlSource, original);
            return buildCount(sqlSource);
        }
        if(original instanceof StaticSqlSource) {
            String sql = (String) ReflectionUtils.getField(sqlFieldInStaticSqlSource, original);
            Configuration configuration = (Configuration) ReflectionUtils.getField(configurationFieldInStaticSqlSource, original);
            List<ParameterMapping> parameterMappings = (List<ParameterMapping>) ReflectionUtils.getField(parameterMappingsFieldInStaticSqlSource, original);
            String countSql = createStaticCountSql(sql);
            return new StaticSqlSource(configuration, countSql, parameterMappings);
        }
        if(original instanceof DynamicSqlSource) {
            SqlNode rootSqlNode = (SqlNode) ReflectionUtils.getField(rootSqlNodeFieldInDynamicSqlSource, original);
            Configuration configuration = (Configuration) ReflectionUtils.getField(configurationFieldInDynamicSqlSource, original);
            SqlNode countSqlNode = createCountSqlNode(rootSqlNode);
            return new DynamicSqlSource(configuration, countSqlNode);
        }
        //
        throw new UnsupportedOperationException(original.getClass().getName());
    }

    private static final Pattern fromPattern = Pattern.compile("\\s+from\\s+", Pattern.CASE_INSENSITIVE);
    private SqlNode createCountSqlNode(SqlNode rootSqlNode) {
        if(rootSqlNode instanceof MixedSqlNode) {
            boolean found = false;
            List<SqlNode> contents = (List<SqlNode>) ReflectionUtils.getField(contentsFieldInMixedSqlNode, rootSqlNode);
            List<SqlNode> newContents = new ArrayList<SqlNode>();
            for(SqlNode sqlNode : contents) {
                if(!found && sqlNode instanceof StaticTextSqlNode) {
                    String text = (String) ReflectionUtils.getField(textFieldInStaticTextSqlNode, sqlNode);
                    if(!fromPattern.matcher(text).find()) {
                        continue;
                    }
                    String countSqlFragment = createStaticCountSql(text);
                    newContents.add(new StaticTextSqlNode(countSqlFragment));
                    //
                    found = true;
                } else {
                    newContents.add(sqlNode);
                }
            }
            return new MixedSqlNode(newContents);
        }
        //
        throw new UnsupportedOperationException(rootSqlNode.getClass().getName());
    }

    private String createStaticCountSql(String sql) {
        Matcher matcher = fromPattern.matcher(sql);
        if(matcher.find()) {
            return "select count(1) " + sql.substring(matcher.start());
        }
        throw new NullPointerException(String.format("Sql key word 'from' not found in sql [%s]", sql));
    }

    @Override
    public SqlSource buildPage(SqlSource original) {
        //
        if(original instanceof RawSqlSource) {
            SqlSource sqlSource = (SqlSource) ReflectionUtils.getField(sqlSourceFieldInRawSqlSource, original);
            return buildPage(sqlSource);
        }
        if(original instanceof StaticSqlSource) {
            String sql = (String) ReflectionUtils.getField(sqlFieldInStaticSqlSource, original);
            Configuration configuration = (Configuration) ReflectionUtils.getField(configurationFieldInStaticSqlSource, original);
            List<ParameterMapping> parameterMappings = (List<ParameterMapping>) ReflectionUtils.getField(parameterMappingsFieldInStaticSqlSource, original);
            //
            String pageSql = createStaticPageSql(sql);
            List<ParameterMapping> pageParameterMappings = new ArrayList<ParameterMapping>(parameterMappings);
            pageParameterMappings.add(new ParameterMapping.Builder(configuration, "limit", Integer.class).build());
            pageParameterMappings.add(new ParameterMapping.Builder(configuration, "offset", Integer.class).build());
            return new StaticSqlSource(configuration, pageSql, pageParameterMappings);
        }
        if(original instanceof DynamicSqlSource) {
            SqlNode rootSqlNode = (SqlNode) ReflectionUtils.getField(rootSqlNodeFieldInDynamicSqlSource, original);
            Configuration configuration = (Configuration) ReflectionUtils.getField(configurationFieldInDynamicSqlSource, original);
            SqlNode pageSqlNode = createPageSqlNode(rootSqlNode);
            return new DynamicSqlSource(configuration, pageSqlNode);
        }
        //
        throw new UnsupportedOperationException(original.getClass().getName());
    }

    private String createStaticPageSql(String sql) {
        return sql + " limit ? offset ?";
    }

    private String createDynamicPageSql(String sql) {
        return sql + " limit #{limit} offset #{offset}";
    }

    private SqlNode createPageSqlNode(SqlNode rootSqlNode) {
        //
        if(rootSqlNode instanceof MixedSqlNode) {
            List<SqlNode> contents = (List<SqlNode>) ReflectionUtils.getField(contentsFieldInMixedSqlNode, rootSqlNode);
            List<SqlNode> newContents = new ArrayList<SqlNode>(contents);
            String pageSql = createDynamicPageSql("");
            newContents.add(new StaticTextSqlNode(pageSql));
            return new MixedSqlNode(newContents);
        }
        //
        throw new UnsupportedOperationException(rootSqlNode.getClass().getName());
    }
}
