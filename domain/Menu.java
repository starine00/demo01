package com.mhl.domain;

/*
这是一个JavaBean和Menu表对应
    create table menu (
	id int primary key auto_increment, #自增主键，作为菜谱编号(唯一)
	name varchar(50) not null default '',#菜品名称
	type varchar(50) not null default '', #菜品种类
	price double not null default 0#价格
)charset=utf8;
 */
public class Menu {
    private Integer id;
    private String name;
    private String type;
    private Double price;

    @Override
    public String toString() {
        return id+"\t\t\t"+name+"\t\t"+type+"\t\t"+price;
    }

    public Menu() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
