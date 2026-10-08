import request from "./request";
// ScController - 选课记录 / 成绩管理
export function getScByStudent(studentId){
    return request.get(`/sc/student/${studentId}`)
}
export function getScByCourse(courseId){
    return request.get(`/sc/course/${courseId}`)
}
export function updateScore(scId, score){
    return request.put('/sc/score', null, { params: { scId, score } })
}
export function getScList(){
    return request.get('/sc/list')
}
