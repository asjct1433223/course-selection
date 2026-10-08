package com.example.course.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.course.entity.Student;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 学生 Mapper
 */
public interface StudentMapper extends BaseMapper<Student> {

    /**
     * 分页查询学生并关联出登录账号（支持按姓名/学号模糊搜索）
     */
    @Select({"<script>",
            "SELECT s.*, u.username FROM student s",
            "LEFT JOIN sys_user u ON s.user_id = u.id",
            "<where>",
            "<if test='keyword != null and keyword != \"\"'>",
            "  (s.name LIKE CONCAT('%', #{keyword}, '%')",
            "   OR s.student_no LIKE CONCAT('%', #{keyword}, '%'))",
            "</if>",
            "</where>",
            "ORDER BY s.id DESC",
            "</script>"})
    IPage<Student> selectPageWithUser(Page<Student> page, @Param("keyword") String keyword);

    /**
     * 查询所有学生并关联出登录账号
     */
    @Select("SELECT s.*, u.username FROM student s LEFT JOIN sys_user u ON s.user_id = u.id ORDER BY s.id DESC")
    List<Student> selectListWithUser();

    /**
     * 按主键查询学生并关联出登录账号
     */
    @Select("SELECT s.*, u.username FROM student s LEFT JOIN sys_user u ON s.user_id = u.id WHERE s.id = #{id}")
    Student selectByIdWithUser(@Param("id") Long id);

    /**
     * 按用户ID查询学生（登录后获取当前学生）
     */
    @Select("SELECT * FROM student WHERE user_id = #{userId}")
    Student selectByUserId(@Param("userId") Long userId);
}
