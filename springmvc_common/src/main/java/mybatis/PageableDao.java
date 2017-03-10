package mybatis;

public interface PageableDao<M> extends BaseDao<M> {

    /**
     * 分页查找
     * @param page
     * @return
     */
    Page<M> findPage(Page<M> page);

    /**
     * 分页查找根据 example 过滤
     * @param page
     * @param example
     * @return
     */
    Page<M> findPageByExample(Page<M> page, M example);

}
