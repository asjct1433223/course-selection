package com.example.course.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.course.entity.Teacher;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 教师 Mapper
 */
public interface TeacherMapper extends BaseMapper<Teacher> {

    /** 分页查询教师并关联出登录账号（支持按姓名/工号模糊搜索） */
    @Select({"<script>",
            "SELECT t.*, u.username FROM teacher t",
            "LEFT JOIN sys_user u ON t.user_id = u.id",
            "<where>",
            "<if test='keyword != null and keyword != \"\"'>",
            "  (t.name LIKE CONCAT('%', #{keyword}, '%')",
            "   OR t.teacher_no LIKE CONCAT('%', #{keyword}, '%'))",
            "</if>",
            "</where>",
            "ORDER BY t.id DESC",
            "</script>"})
    IPage<Teacher> selectPageWithUser(Page<Teacher> page, @Param("keyword") String keyword);

    /** 查询所有教师并关联出登录账号 */
    @Select("SELECT t.*, u.username FROM teacher t LEFT JOIN sys_user u ON t.user_id = u.id ORDER BY t.id DESC")
    List<Teacher> selectListWithUser();

    /** 按主键查询教师并关联出登录账号 */
    @Select("SELECT t.*, u.username FROM teacher t LEFT JOIN sys_user u ON t.user_id = u.id WHERE t.id = #{id}")
    Teacher selectByIdWithUser(@Param("id") Long id);

    /** 按用户ID查询教师（登录后获取当前教师） */
    @Select("SELECT * FROM teacher WHERE user_id = #{userId}")
    Teacher selectByUserId(@Param("userId") Long userId);
}
