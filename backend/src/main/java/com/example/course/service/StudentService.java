package com.example.course.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.course.entity.Student;

import java.util.List;

/**
 * 学生服务接口
 */
public interface StudentService extends IService<Student> {

    /** 分页查询学生（含登录账号） */
    IPage<Student> pageWithUser(Integer current, Integer size, String keyword);

    /** 查询所有学生（含登录账号） */
    List<Student> listWithUser();

    /** 按ID查询学生（含登录账号） */
    Student getWithUserById(Long id);

    /** 按用户ID查询学生 */
    Student getByUserId(Long userId);

    /** 新增学生（同时创建登录账号） */
    boolean addStudent(Student student);

    /** 修改学生信息 */
    boolean updateStudent(Student student);

    /** 删除学生（级联删除选课记录与登录账号） */
    boolean deleteStudent(Long id);
}
