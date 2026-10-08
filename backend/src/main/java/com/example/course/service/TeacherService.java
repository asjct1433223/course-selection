package com.example.course.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.course.entity.Teacher;

import java.util.List;

/**
 * 教师服务接口
 */
public interface TeacherService extends IService<Teacher> {

    /** 分页查询教师（含登录账号） */
    IPage<Teacher> pageWithUser(Integer current, Integer size, String keyword);

    /** 查询所有教师（含登录账号） */
    List<Teacher> listWithUser();

    /** 按ID查询教师（含登录账号） */
    Teacher getWithUserById(Long id);

    /** 按用户ID查询教师 */
    Teacher getByUserId(Long userId);

    /** 新增教师（同时创建登录账号） */
    boolean addTeacher(Teacher teacher);

    /** 修改教师信息 */
    boolean updateTeacher(Teacher teacher);

    /** 删除教师（有关联课程的教师不允许删除） */
    boolean deleteTeacher(Long id);
}
