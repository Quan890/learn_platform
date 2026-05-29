package com.example.xdlearnbackend.service.impl;

import com.example.xdlearnbackend.common.PageResult;
import com.example.xdlearnbackend.dto.CourseDTO;
import com.example.xdlearnbackend.entity.Course;
import com.example.xdlearnbackend.entity.CourseType;
import com.example.xdlearnbackend.entity.User;
import com.example.xdlearnbackend.exception.BusinessException;
import com.example.xdlearnbackend.mapper.CourseMapper;
import com.example.xdlearnbackend.service.CourseService;
import com.example.xdlearnbackend.vo.CourseChapterVO;
import com.example.xdlearnbackend.vo.CourseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 木又
 * @date 2026/5/17 16:13
 */
@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseMapper courseMapper;

    /**
     * 获取课程列表（分页获取）
     * @param query 查询参数
     * @return 课程列表
     */
    @Override
    public PageResult<Course> getPage(CourseDTO query) {
        Course course = new Course();
        if (query != null){
            course.setTitle(query.getTitle());
            course.setDescription(query.getDescription());
            course.setTeacherName(query.getTeacherName());
            if (query.getTypeId() != null){
                CourseType type = new CourseType();
                type.setId(Math.toIntExact(query.getTypeId()));
                course.setType(type);
            }
            if (query.getCreatorId() != null){
                User user = new User();
                user.setId(query.getCreatorId());
                course.setCreator(user);
            }
        }
        //获取总记录数
        Double minRating = query.getMinRating() != null ? query.getMinRating().doubleValue() : null;
        int totalCount = courseMapper.count(course, minRating, query.getMinStudentCount());

        //计算偏移量
        int offset = (query.getPageNum() - 1) * query.getPageSize();

        //获取分页数据
        List<Course> list = courseMapper.findListByPage(course,offset,query.getPageSize());

        //封装分页结果
        PageResult<Course> pageResult = new PageResult<>(list, totalCount, query.getPageNum(), query.getPageSize());

        return pageResult;
    }

    /**
     * 获取课程详情
     * @param id 课程ID
     * @return 课程详情
     */
    @Override
    public CourseVO getCourseDetail(Long id) {
        Course course = courseMapper.findById(id);
        if (course == null) {
            throw new BusinessException("课程不存在");
        }

        CourseVO courseVO = new CourseVO();
        courseVO.setId(course.getId());
        courseVO.setTitle(course.getTitle());
        courseVO.setDescription(course.getDescription());
        courseVO.setAuthor(course.getTeacherName());

        if (course.getType() != null) {
            courseVO.setType(course.getType().getName());
        }

        if (course.getRating() != null) {
            courseVO.setRating(course.getRating().doubleValue());
        }

        if (course.getStudentCount() != null) {
            courseVO.setStudents(course.getStudentCount().intValue());
        }

        courseVO.setPrice(course.getPrice());
        courseVO.setCover(course.getCoverImage());

        courseVO.setGoals(courseMapper.findLearningObjectivesByCourseId(id));
        courseVO.setAudience(courseMapper.findTargetAudienceByCourseId(id));
        courseVO.setChapters(courseMapper.findChaptersByCourseId(id));

        courseVO.setCreateTime(course.getCreatedTime());
        courseVO.setUpdateTime(course.getUpdatedTime());

        return courseVO;
    }
}
