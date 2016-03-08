# HiDo


###这是一个java操作MYSQL数据库的小型封装工具，将sql语句与代码分离，只要配置每张表的字段即可实现自动建表和各种增删改查等操作，不需使用实体对象，使用方便灵活。


如: 

//打开配置中的某张表
HiDo hide = HiDo.open("tableName");
//建表
hido.create();
//添加数据
hido.add("xxx","xxx");
或
Entity entity = new Entity();
entity.setValue("hido");
hido.add(entity);
...

