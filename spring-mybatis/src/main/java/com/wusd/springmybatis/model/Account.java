package com.wusd.springmybatis.model;

public class Account {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column account.id
     *
     * @mbggenerated
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column account.money
     *
     * @mbggenerated
     */
    private Double money;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column account.name
     *
     * @mbggenerated
     */
    private String name;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column account.id
     *
     * @return the value of account.id
     *
     * @mbggenerated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column account.id
     *
     * @param id the value for account.id
     *
     * @mbggenerated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column account.money
     *
     * @return the value of account.money
     *
     * @mbggenerated
     */
    public Double getMoney() {
        return money;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column account.money
     *
     * @param money the value for account.money
     *
     * @mbggenerated
     */
    public void setMoney(Double money) {
        this.money = money;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column account.name
     *
     * @return the value of account.name
     *
     * @mbggenerated
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column account.name
     *
     * @param name the value for account.name
     *
     * @mbggenerated
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }
}