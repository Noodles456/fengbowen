"# fengbowen" 
mvn -Dmybatis.generator.overwrite=true mybatis-generator:generate
@ResponseBody反序列化后端对象成json
@RequestBody序列化前端json成对象
json通常用于与服务端交换数据，
在向服务器发送数据时一般是字符串，
我们可以使用JSON.Stringify方法将JavaScript对象转换为字符串

