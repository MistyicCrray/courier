package com.xhr.courier.model;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Transient;

public class Contract {
	/**
	 *
	 * This field was generated by MyBatis Generator. This field corresponds to
	 * the database column t_contract.id
	 *
	 * @mbg.generated Sat Feb 01 12:12:41 CST 2020
	 */
	private Integer id;

	/**
	 *
	 * This field was generated by MyBatis Generator. This field corresponds to
	 * the database column t_contract.user_id
	 *
	 * @mbg.generated Sat Feb 01 12:12:41 CST 2020
	 */
	private Integer userId;

	/**
	 *
	 * This field was generated by MyBatis Generator. This field corresponds to
	 * the database column t_contract.to_user_id
	 *
	 * @mbg.generated Sat Feb 01 12:12:41 CST 2020
	 */
	private Integer toUserId;

	/**
	 *
	 * This field was generated by MyBatis Generator. This field corresponds to
	 * the database column t_contract.create_time
	 *
	 * @mbg.generated Sat Feb 01 12:12:41 CST 2020
	 */
	private Date createTime;

	@Transient
	private List<ContractContent> contents;

	public List<ContractContent> getContents() {
		return contents;
	}

	public void setContents(List<ContractContent> contents) {
		this.contents = contents;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the
	 * value of the database column t_contract.id
	 *
	 * @return the value of t_contract.id
	 *
	 * @mbg.generated Sat Feb 01 12:12:41 CST 2020
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the
	 * value of the database column t_contract.id
	 *
	 * @param id
	 *            the value for t_contract.id
	 *
	 * @mbg.generated Sat Feb 01 12:12:41 CST 2020
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the
	 * value of the database column t_contract.user_id
	 *
	 * @return the value of t_contract.user_id
	 *
	 * @mbg.generated Sat Feb 01 12:12:41 CST 2020
	 */
	public Integer getUserId() {
		return userId;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the
	 * value of the database column t_contract.user_id
	 *
	 * @param userId
	 *            the value for t_contract.user_id
	 *
	 * @mbg.generated Sat Feb 01 12:12:41 CST 2020
	 */
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the
	 * value of the database column t_contract.to_user_id
	 *
	 * @return the value of t_contract.to_user_id
	 *
	 * @mbg.generated Sat Feb 01 12:12:41 CST 2020
	 */
	public Integer getToUserId() {
		return toUserId;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the
	 * value of the database column t_contract.to_user_id
	 *
	 * @param toUserId
	 *            the value for t_contract.to_user_id
	 *
	 * @mbg.generated Sat Feb 01 12:12:41 CST 2020
	 */
	public void setToUserId(Integer toUserId) {
		this.toUserId = toUserId;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the
	 * value of the database column t_contract.create_time
	 *
	 * @return the value of t_contract.create_time
	 *
	 * @mbg.generated Sat Feb 01 12:12:41 CST 2020
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the
	 * value of the database column t_contract.create_time
	 *
	 * @param createTime
	 *            the value for t_contract.create_time
	 *
	 * @mbg.generated Sat Feb 01 12:12:41 CST 2020
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}