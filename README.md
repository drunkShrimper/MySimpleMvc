# MyFramework

## 还未实现的功能
- restful风格
- 路径变量
- 使用对象接收请求参数 对象属性 必须 和 form表单中的属性Name值一致（支持级联属性）
```html
<form action="handler/testObjectProperties" method="post">
    id:<input name="id" type="text" />
    name:<input name="name" type="text" />
    家庭地址:<input name="address.homeAddress" type="text" />
    学校地址:<input name="address.schoolAddress" type="text" />
    <input type="submit" value="查">
</form>

```java
@RequestMapping(value="testObjectProperties")
//student属性 必须 和 form表单中的属性Name值一致（支持级联属性）
public String  testObjectProperties(Student student) {
    System.out.println(student.getId() ....);
    return "success" ;
}

## 目前存在的问题
- 在maven配置的tomcat7插件运行报错，但是使用本地tomcat正常


