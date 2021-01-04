package com.dzero.malphite.data.jpa.repository.support;

import com.dzero.malphite.data.jpa.repository.MalphiteJpaRepository;
import org.hibernate.Session;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.SingularAttribute;
import java.util.ArrayList;
import java.util.List;

@Repository
public class SimpleMalphiteJpaRepository<T, ID> extends SimpleJpaRepository<T, ID> implements MalphiteJpaRepository<T, ID> {

    private static final String ENTITY_NULL_MSG = "Entity must not be null!";

    private static final String ENTITIES_NULL_MSG = "Entities must not be null!";

    private final JpaEntityInformation<T, ?> entityInformation;

    private final EntityManager em;

    /**
     * 构造方法.
     *
     * @param entityInformation JPA 实体信息类，不能为 {@literal null}.
     * @param entityManager     实体管理器类，不能为 {@literal null}.
     */
    public SimpleMalphiteJpaRepository(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityInformation = entityInformation;
        this.em = entityManager;
    }

    /**
     * 构造方法.
     *
     * @param domainClass JPA 实体类的 class，不能为 {@literal null}.
     * @param em          实体管理器类，不能为 {@literal null}.
     */
    public SimpleMalphiteJpaRepository(Class<T> domainClass, EntityManager em) {
        this(JpaEntityInformationSupport.getEntityInformation(domainClass, em), em);
    }

    /**
     * 新增单条实体类
     *
     * @param entity 实体类
     * @return 实体类
     */
    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public <S extends T> S add(S entity) {
        this.em.persist(entity);
        return entity;
    }

    /**
     * 批量新增实体类集合
     *
     * @param entities 实体类集合
     * @return 影响个数
     */
    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public <S extends T> int addAll(Iterable<S> entities) {
        return this.addAll(entities, 200);
    }

    /**
     * 批量新增实体类集合
     *
     * @param entities  实体类集合
     * @param batchSize 批量大小
     * @return 影响个数
     */
    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public <S extends T> int addAll(Iterable<S> entities, int batchSize) {
        Assert.notNull(entities, ENTITIES_NULL_MSG);
        int i = 0;
        for (S entity : entities) {
            this.em.persist(entity);
            if (++i % batchSize == 0) {
                this.em.flush();
                this.em.clear();
            }
        }
        return i;
    }

    /**
     * 单条数据的更新
     *
     * @param entity 实体类
     * @return 实体类
     */
    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public <S extends T> S update(S entity) {
        Assert.notNull(entity, ENTITY_NULL_MSG);
        return this.em.merge(entity);
    }

    /**
     * 批量更新实体类集合
     *
     * @param entities 实体类集合
     * @return 影响个数
     */
    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public <S extends T> int updateAll(Iterable<S> entities) {
        return this.updateAll(entities, 200);
    }

    /**
     * 批量更新实体类集合
     *
     * @param entities  实体类集合
     * @param batchSize 批量大小
     * @return 影响个数
     */
    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public <S extends T> int updateAll(Iterable<S> entities, int batchSize) {
        Assert.notNull(entities, ENTITIES_NULL_MSG);
        int i = 0;
        Session session = this.em.unwrap(Session.class);
        for (S entity : entities) {
            session.update(entity);
            if (++i % batchSize == 0) {
                this.em.flush();
                this.em.clear();
            }
        }
        return i;
    }

    /**
     * 根据ID主键批量删除表数据
     *
     * @param ids ID主键集合
     * @return 影响个数
     */
    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public int deleteAllByIds(Iterable<ID> ids) {
        return 0;
    }

    /**
     * 根据ID主键批量删除表数据
     *
     * @param ids       ID主键集合
     * @param batchSize 批量大小
     * @return 影响个数
     */
    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public int deleteAllByIds(Iterable<ID> ids, int batchSize) {
        Assert.notNull(ids, "The given ids must not be null!");
        Assert.isTrue(batchSize > 0, "The given batchSize must not be <= 0.");

        // 获取到实体名称和 ID 属性名称，并生成用于批量删除的 in 条件 SQL.
        final String entityName = this.entityInformation.getEntityName();
        SingularAttribute<? super T, ?> idAttribute = this.entityInformation.getIdAttribute();
        final String idName = idAttribute == null ? "id" : idAttribute.getName();
        String sql = stringFormat("delete from {} where {} in :batch_ids", entityName, idName);

        int i = 0;
        List<ID> batchIds = new ArrayList<>();
        for (ID id : ids) {
            if (id == null) {
                continue;
            }

            batchIds.add(id);
            if (++i % batchSize == 0 && !batchIds.isEmpty()) {
                this.doBatchDelete(sql, batchIds);
                batchIds.clear();
            }
        }

        // 如果最后 batchIds 不为空，则再继续删除剩余的数据.
        if (!batchIds.isEmpty()) {
            this.doBatchDelete(sql, batchIds);
        }
        return i;
    }

    /**
     * 真正执行批量删除的方法.
     *
     * @param sql      in 条件的删除 SQL
     * @param batchIds 要批量删除的 ID 数据
     */
    private void doBatchDelete(String sql, List<ID> batchIds) {
        this.em.createQuery(sql)
                .setParameter("batch_ids", batchIds)
                .executeUpdate();
    }

    public static String stringFormat(String pattern, Object... args) {
        return pattern == null ? "" : MessageFormatter.arrayFormat(pattern, args).getMessage();
    }
}
