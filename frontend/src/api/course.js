import request from "./request";
// CourseController - 课程管理
export function getCoursePage(params){
    return request.get('/course/page',{params})
}
export function getCourseList(){
    return request.get('/course/list')
}
export function getCourseById(id){
    return request.get(`/course/${id}`)
}
export function saveCourse(data){
    return request.post('/course',data)
}
export function updateCourse(data){
    return request.put('/course',data)
}
export function deleteCourse(id){
    return request.delete(`/course/${id}`)
}
// 选课 / 退课
export function selectCourse(studentId, courseId){
    return request.post('/course/select', null, { params: { studentId, courseId } })
}
export function dropCourse(studentId, courseId){
    return request.post('/course/drop', null, { params: { studentId, courseId } })
}
