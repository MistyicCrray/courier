package com.xhr.courier.dao;

import com.xhr.courier.model.Contract;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ContractMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_contract
     *
     * @mbg.generated Sat Feb 01 12:12:41 CST 2020
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_contract
     *
     * @mbg.generated Sat Feb 01 12:12:41 CST 2020
     */
    int insert(Contract record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_contract
     *
     * @mbg.generated Sat Feb 01 12:12:41 CST 2020
     */
    Contract selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_contract
     *
     * @mbg.generated Sat Feb 01 12:12:41 CST 2020
     */
    List<Contract> selectAll();

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_contract
     *
     * @mbg.generated Sat Feb 01 12:12:41 CST 2020
     */
    int updateByPrimaryKey(Contract record);
    
    List<Map<String, Object>> findByUserId(int userId);
    
    Contract findByUserIdAndToUserId(Map<String, Object> map);
}