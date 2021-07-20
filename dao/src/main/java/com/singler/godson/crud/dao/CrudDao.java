package com.singler.godson.crud.dao;

import com.singler.godson.crud.common.enums.BooleanEnum;
import com.singler.godson.crud.domain.entities.IBasicEntity;
import com.singler.godson.hibatis.dao.HibatisDao;
import com.singler.godson.hibatis.orderby.OrderBy;
import com.singler.godson.hibatis.where.WhereClause;

/**
 *  基础增删改查Dao
 *
 * @author maenfang1
 * @version 1.0
 * @date 2020/5/24 14:14
 */
public interface CrudDao<ID, ENTITY extends IBasicEntity<ID>> extends HibatisDao<ENTITY, ID> {
    String ID = "id";
    String YN = "yn";
    String VERSION  = "version";
    String CREATED  = "created";
    String MODIFIED = "modified";
    String CREATOR_ID  = "creator";
    String MODIFIER_ID = "modifier";
    String PLATFORM_ID = "platformId";

    WhereClause WHERE_CLAUSE_ID = WhereClause.newClause(ID);
    WhereClause WHERE_CLAUSE_YN_NO  = WhereClause.newClause(YN).eq(BooleanEnum.FALSE.intValue());
    WhereClause WHERE_CLAUSE_YN_YES = WhereClause.newClause(YN).eq(BooleanEnum.TRUE.intValue());
    WhereClause WHERE_CLAUSE_CREATOR_ID  = WhereClause.newClause(CREATOR_ID);
    WhereClause WHERE_CLAUSE_MODIFIER_ID = WhereClause.newClause(MODIFIER_ID);
    WhereClause WHERE_CLAUSE_PLATFORM_ID = WhereClause.newClause(PLATFORM_ID);

    OrderBy CREATED_ASC  = OrderBy. asc(CREATED);
    OrderBy CREATED_DESC = OrderBy.desc(CREATED);

}
