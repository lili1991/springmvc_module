package mybatis;


import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import java.beans.PropertyDescriptor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyBatisDaoSupport<M> extends SqlSessionDaoSupport implements PageableDao<M> {

    private String idPrefix;

    public MyBatisDaoSupport() {
        inspectIdPrefix();
    }

    public String getIdPrefix() {
        return idPrefix;
    }

    public void setIdPrefix(String idPrefix) {
        this.idPrefix=idPrefix;
    }

    @Autowired
    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        super.setSqlSessionFactory(sqlSessionFactory);
    }


    public void create(M model) {
        this.insert("insert", model);
    }


    public M retrieve(Long id) {
        return this.selectOne("select", id);
    }


    public void update(M model) {
        this.update("update", model);
    }


    public void delete(Long id) {
        this.delete("delete", id);
    }


    public List<M> findAll() {
        return this.selectList("selectAll");
    }


    public M findOneByExample(M example) {
        List<M> results = findByExample(example);
        if(results.size()>1) {
            throw new IllegalArgumentException("Result is more than one!");
        }
        if(results.size()==1) {
            return results.get(0);
        }
        return null;
    }


    public List<M> findByExample(M model) {
        return this.selectList("selectByExample", model);
    }


    public Page<M> findPage(Page<M> page) {
        return this.selectPage("selectPage", page);
    }


    public Page<M> findPageByExample(Page<M> page, M example) {
        Map parameter = new HashMap();
        //
        BeanWrapper beanWrapper = new BeanWrapperImpl(example);
        for(PropertyDescriptor pd : beanWrapper.getPropertyDescriptors()) {
            if(beanWrapper.isReadableProperty(pd.getName())) {
                Object value = beanWrapper.getPropertyValue(pd.getName());
                parameter.put(pd.getName(), value);
            }
        }
        //
        return this.selectPage("selectPageByExample", page, parameter);
    }

    protected int insert(String insertId, Object parameter)  {
        return getSqlSession().insert(idPrefix.concat(insertId), parameter);
    }

    protected int update(String updateId, Object parameter)  {
        return getSqlSession().update(idPrefix.concat(updateId), parameter);
    }

    protected int delete(String deleteId, Object parameter)  {
        return getSqlSession().delete(idPrefix.concat(deleteId), parameter);
    }

    protected <M> M selectOne(String selectId, Object parameter) {
        return getSqlSession().selectOne(idPrefix.concat(selectId), parameter);
    }

    protected List<M> selectList(String selectId) {
        return getSqlSession().selectList(idPrefix.concat(selectId));
    }

    protected List<M> selectList(String selectId, Object parameter) {
        return getSqlSession().selectList(idPrefix.concat(selectId), parameter);
    }

    protected Page<M> selectPage(String selectId, Page<M> page) {
        return this.selectPage(selectId, page, new HashMap<String, Object>());
    }

    protected Page<M> selectPage(String selectId, Page<M> page, Map<String, Object> parameter) {
        //
        if(page.isAutoCount()) {
            selectPageCount(page, selectId, parameter);
        }
        //
        setPageParameter(parameter, page);
        //
        List<M> results = this.selectList(selectId, parameter);
        return page.setResults(results);
    }

    private void setPageParameter(Map<String, Object> parameter, Page<M> page) {
        parameter.put("limit", page.getPageSize());
        parameter.put("offset", page.getFirst()-1);
    }

    private void selectPageCount(Page<M> page, String selectId, Object parameter) {
        String countId = selectId.concat("Count");
        List<Long> countResults = (List<Long>) this.selectList(countId, parameter);
        // FIX Group By Statement
        Long totalCount = 0L;
        for(Long countResult : countResults) {
            totalCount += countResult;
        }
        page.setTotalCount(totalCount);
    }

    private void inspectIdPrefix() {
        Class<?>[] interfaceClasses = ClassUtils.getAllInterfaces(this);
        for (Class<?> interfaceClass : interfaceClasses) {
            if (interfaceClass.getSimpleName().endsWith("Dao")) {
                this.idPrefix = interfaceClass.getName();
                break;
            }
        }
        //
        Assert.notNull(idPrefix, "Dao interface name must ends with Dao!");
        //
        this.idPrefix += ".";
    }

}