使用javah生成jni文件方法：
使用android studio的Preference>Tools> External Tools来生成
配置信息：
Program：/usr/bin/javah （Whereis javah 查看具体的命令路径） 
Parameters：-classpath . -jni -d $ModuleFileDir$/src/main/jni $FileClass$
working directory：$ModuleFileDir$/src/main/java
参考网站：https://blog.csdn.net/lw0328/article/details/78754682

使用参考：https://www.jianshu.com/p/b4431ac22ec2