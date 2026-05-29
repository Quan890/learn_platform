package com.example.xdlearnbackend.controller;

import com.example.xdlearnbackend.common.PageResult;
import com.example.xdlearnbackend.common.Result;
import com.example.xdlearnbackend.dto.CourseDTO;
import com.example.xdlearnbackend.entity.Course;
import com.example.xdlearnbackend.mapper.CourseMapper;
import com.example.xdlearnbackend.service.CourseResourceService;
import com.example.xdlearnbackend.service.CourseService;
import com.example.xdlearnbackend.vo.CourseChapterVO;
import com.example.xdlearnbackend.vo.CourseListVO;
import com.example.xdlearnbackend.vo.CourseResourceVO;
import com.example.xdlearnbackend.vo.CourseVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;


    @Autowired
    private CourseResourceService courseResourceService;

    @Autowired
    private CourseMapper courseMapper;

    /**
     * 获取课程列表
     * @param keyword 课程标题关键词
     * @param typeId 课程类型ID
     * @param page 页码
     * @param pageSize 每页数量
     * @return 课程列表
     */
    @GetMapping
    public Result<CourseListVO> getCourseList(
       @RequestParam (required = false)String keyword,
       @RequestParam (required = false)Long typeId,
       @RequestParam (defaultValue = "1")Integer page,
       @RequestParam (defaultValue = "9")Integer pageSize
    ) {
        CourseDTO query = new CourseDTO();
        query.setPageNum( page);
        query.setPageSize(pageSize);
        query.setTitle(keyword);
        query.setTypeId(typeId);

        PageResult<Course> pageResult = courseService.getPage(query);
        List<CourseVO> courseVOList = converToCourseVOList(pageResult.getData());

        CourseListVO courseListVO = new CourseListVO();
        courseListVO.setList(courseVOList);
        courseListVO.setTotal(pageResult.getTotal());
        courseListVO.setPage(page);
        courseListVO.setPageSize(pageSize);

        return Result.success("success",courseListVO);
    }

    /**
     * 获取课程详情
     * @param id 课程ID
     * @return 课程详情
     */
    @GetMapping("/{id}/detail")
    public Result<CourseVO> getCourseDetail(@PathVariable Long id) {
        CourseVO courseDetail = courseService.getCourseDetail(id);
        return Result.success("获取课程详情成功", courseDetail);
    }

    /**
     * 获取课程资料
     * @param courseId 课程ID
     * @return 课程资料
     */
    @GetMapping("/{courseId}/materials")
    public Result<List<CourseResourceVO>> getCourseMaterials(@PathVariable Long courseId) {
        List<CourseResourceVO> materials = courseResourceService.getCourseResources(courseId);
        return Result.success("获取课程资料成功", materials);
    }

    /**
     * 获取课程章节
     * @param courseId 课程ID
     * @return 课程章节
     */
    @GetMapping("/{courseId}/chapters")
    public Result<List<CourseChapterVO>> getCourseChapters(@PathVariable Long courseId) {
        List<CourseChapterVO> chapters = courseMapper.findChaptersByCourseId(courseId);
        return Result.success("获取课程章节成功", chapters);
    }

    /**
     * 课程列表转换成VO列表
     * @param coursesList 课程列表
     * @return 课程VO列表
     */
    private List<CourseVO> converToCourseVOList(List< Course>  coursesList) {
        List<CourseVO> courseVOList = new ArrayList<>();

        for (Course course : coursesList) {
            CourseVO courseVO = new CourseVO();
            BeanUtils.copyProperties(course, courseVO);
            courseVO.setAuthor(course.getTeacherName());
            if (course.getType() != null){
                courseVO.setType(course.getType().getName());
            }
            if (course.getRating() != null){
                courseVO.setRating(course.getRating().doubleValue());
            }
            if (course.getStudentCount() != null){
                courseVO.setStudents(course.getStudentCount().intValue());
            }
            courseVO.setCover(course.getCoverImage());
            courseVO.setCreateTime(course.getCreatedTime());
            courseVOList.add(courseVO);
        }
        return  courseVOList;
    }

}
