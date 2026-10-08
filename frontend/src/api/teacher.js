import request from "./request";
// TeacherController - 教师管理
export function getTeacherPage(params){
    return request.get('/teacher/page',{params})
}
export function getTeacherList(){
    return request.get('/teacher/list')
}
export function getTeacherById(id){
    return request.get(`/teacher/${id}`)
}
export function saveTeacher(data){
    return request.post('/teacher',data)
}
export function updateTeacher(data){
    return request.put('/teacher',data)
}
export function deleteTeacher(id){
    return request.delete(`/teacher/${id}`)
}
