package com.example.course.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.course.entity.Course;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 课程 Mapper
 */
public interface CourseMapper extends BaseMapper<Course> {

    /** 分页查询课程并关联出授课教师姓名（支持按课程名称/编号模糊搜索） */
    @Select({"<script>",
            "SELECT c.*, t.name AS teacher_name FROM course c",
            "LEFT JOIN teacher t ON c.teacher_id = t.id",
            "<where>",
            "<if test='keyword != null and keyword != \"\"'>",
            "  (c.name LIKE CONCAT('%', #{keyword}, '%')",
            "   OR c.course_no LIKE CONCAT('%', #{keyword}, '%'))",
            "</if>",
            "</where>",
            "ORDER BY c.id DESC",
            "</script>"})
    IPage<Course> selectPageWithTeacher(Page<Course> page, @Param("keyword") String keyword);

    /** 查询所有课程并关联出授课教师姓名 */
    @Select("SELECT c.*, t.name AS teacher_name FROM course c LEFT JOIN teacher t ON c.teacher_id = t.id ORDER BY c.id DESC")
    List<Course> selectListWithTeacher();

    /** 按主键查询课程并关联出授课教师姓名 */
    @Select("SELECT c.*, t.name AS teacher_name FROM course c LEFT JOIN teacher t ON c.teacher_id = t.id WHERE c.id = #{id}")
    Course selectByIdWithTeacher(@Param("id") Long id);

    /** 统计某教师授课的课程数（删除教师前校验） */
    @Select("SELECT COUNT(*) FROM course WHERE teacher_id = #{teacherId}")
    Long countByTeacherId(@Param("teacherId") Long teacherId);
}
