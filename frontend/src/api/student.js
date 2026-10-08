import request from "./request";
//StudentController
export function getStudentList(){
    return request.get('/student/list')   
}
// 查询学生信息(分页查询)  参数表示页数和模糊查询
export function getStudentPage(params){
    return request.get('/student/page',{params})
}
// 根据学生id查询学生信息
export function getStudentById(id){
    return request.get(`/student/${id}`) //使用反引号来传递参数
}
// 修改学生信息
export function updateStudent(data){
    return request.put('/student',data)
}
// 新增学生信息
export function saveStudent(data){
    return request.post('/student',data)
}
// 根据id删除学生信息
export function deleteStudent(id){
     return request.delete(`/student/${id}`) //使用反引号来传递参数
}