package com.example.course.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.course.entity.Sc;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 选课记录 Mapper
 */
public interface ScMapper extends BaseMapper<Sc> {

    /** 查询某学生的选课记录（三表联查：sc + student + course） */
    @Select("SELECT sc.*, s.name AS student_name, s.student_no, c.name AS course_name " +
            "FROM sc " +
            "LEFT JOIN student s ON sc.student_id = s.id " +
            "LEFT JOIN course c ON sc.course_id = c.id " +
            "WHERE sc.student_id = #{studentId} " +
            "ORDER BY sc.id DESC")
    List<Sc> selectByStudentId(@Param("studentId") Long studentId);

    /** 查询某课程的选课学生列表（教师录入成绩用） */
    @Select("SELECT sc.*, s.name AS student_name, s.student_no, c.name AS course_name " +
            "FROM sc " +
            "LEFT JOIN student s ON sc.student_id = s.id " +
            "LEFT JOIN course c ON sc.course_id = c.id " +
            "WHERE sc.course_id = #{courseId} " +
            "ORDER BY sc.id DESC")
    List<Sc> selectByCourseId(@Param("courseId") Long courseId);

    /** 查询全部选课记录（管理员用） */
    @Select("SELECT sc.*, s.name AS student_name, s.student_no, c.name AS course_name " +
            "FROM sc " +
            "LEFT JOIN student s ON sc.student_id = s.id " +
            "LEFT JOIN course c ON sc.course_id = c.id " +
            "ORDER BY sc.id DESC")
    List<Sc> selectAllWithInfo();

    /** 统计某学生是否已选某课程（防重复选课） */
    @Select("SELECT COUNT(*) FROM sc WHERE student_id = #{studentId} AND course_id = #{courseId}")
    long countByStudentAndCourse(@Param("studentId") Long studentId, @Param("courseId") Long courseId);

    /** 录入/修改成绩 */
    @Update("UPDATE sc SET score = #{score} WHERE id = #{id}")
    int updateScore(@Param("id") Long id, @Param("score") Double score);
}
