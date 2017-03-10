package mybatis;

import java.util.List;

public interface BaseDao<M> {

    /**
     * 创建
     * @param model
     */
    void create(M model);

    /**
     * 取回
     * @param id
     * @return
     */
    M retrieve(Long id);

    /**
     * 修改
     * @param model
     */
    void update(M model);

    /**
     * 删除
     * @param id
     */
    void delete(Long id);

    /**
     * 查找所有
     * @return
     */
    List<M> findAll();

    /**
     * 根据 example 查找
     * @param example
     * @return
     */
    M findOneByExample(M example);

    /**
     * 查找所有根据 example 过滤
     * @param example
     * @return
     */
    List<M> findByExample(M example);

}
