package com.singler.godson.crud.domain.entities;

import com.jd.ecc.commons.web.model.BaseEntity;
import com.singler.godson.hibatis.annotation.Id;
import lombok.Data;

/**
 * 基础数据库实体类
 *
 * @author maenfang1
 * @version 1.0
 * @date 2020/4/5 23:39
 */
@Data
public class BasicEntity extends BaseEntity implements IBasicEntity<Long> {
    @Id
    private Long id;
    private Long creator;
    private Long modifier;
    private Integer version;
}
