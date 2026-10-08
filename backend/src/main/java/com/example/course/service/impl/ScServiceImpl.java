package com.example.course.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.course.entity.Sc;
import com.example.course.mapper.ScMapper;
import com.example.course.service.ScService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 选课记录服务实现（成绩管理）
 */
@Service
public class ScServiceImpl extends ServiceImpl<ScMapper, Sc> implements ScService {

    @Override
    public List<Sc> getByStudentId(Long studentId) {
        return baseMapper.selectByStudentId(studentId);
    }

    @Override
    public List<Sc> getByCourseId(Long courseId) {
        return baseMapper.selectByCourseId(courseId);
    }

    @Override
    public List<Sc> listAll() {
        return baseMapper.selectAllWithInfo();
    }

    @Override
    public boolean updateScore(Long scId, Double score) {
        if (scId == null) {
            throw new RuntimeException("选课记录ID不能为空");
        }
        if (score == null || score < 0 || score > 100) {
            throw new RuntimeException("成绩应在0-100分之间");
        }
        if (getById(scId) == null) {
            throw new RuntimeException("选课记录不存在");
        }
        return baseMapper.updateScore(scId, score) > 0;
    }
}
