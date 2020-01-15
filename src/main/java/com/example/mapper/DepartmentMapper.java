package com.example.mapper;

import com.example.beans.Department;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface DepartmentMapper {

    @Select("select * from department where id = #{id}")
    public Department getDeptById(Integer id);

    @Insert("insert into department (name, description, status) values (#{name}, #{description}, #{status}")
    public int insertDept(Department department);
}
