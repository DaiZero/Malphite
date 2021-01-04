package com.dzero.malphite.data.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * 对 {@link JpaRepository} 接口功能的扩展
 *
 * @param <T>  实体类的泛型
 * @param <ID> 实体类的主键ID
 */
@NoRepositoryBean
public interface MalphiteJpaRepository<T, ID> extends JpaRepository<T, ID> {

    /**
     * 新增单条实体类
     *
     * @param entity 实体类
     * @param <S>    泛型实体类
     * @return 实体类
     */
    <S extends T> S add(S entity);

    /**
     * 批量新增实体类集合
     *
     * @param entities 实体类集合
     * @param <S>      泛型实体类
     * @return 影响个数
     */
    <S extends T> int addAll(Iterable<S> entities);

    /**
     * 批量新增实体类集合
     *
     * @param entities  实体类集合
     * @param batchSize 批量大小
     * @param <S>       泛型实体类
     * @return 影响个数
     */
    <S extends T> int addAll(Iterable<S> entities, int batchSize);

    /**
     * 单条数据的更新
     *
     * @param entity 实体类
     * @param <S>    泛型实体类
     * @return 实体类
     */
    <S extends T> S update(S entity);

    /**
     * 批量更新实体类集合
     *
     * @param entities 实体类集合
     * @param <S>      泛型实体类
     * @return 影响个数
     */
    <S extends T> int updateAll(Iterable<S> entities);

    /**
     * 批量更新实体类集合
     *
     * @param entities  实体类集合
     * @param batchSize 批量大小
     * @param <S>       泛型实体类
     * @return 影响个数
     */
    <S extends T> int updateAll(Iterable<S> entities, int batchSize);

    /**
     * 根据ID主键批量删除表数据
     *
     * @param ids ID主键集合
     * @return 影响个数
     */
    int deleteAllByIds(Iterable<ID> ids);

    /**
     * 根据ID主键批量删除表数据
     *
     * @param ids       ID主键集合
     * @param batchSize 批量大小
     * @return 影响个数
     */
    int deleteAllByIds(Iterable<ID> ids, int batchSize);
}
