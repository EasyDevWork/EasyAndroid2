rxlifecycle使用方式：
https://github.com/trello/RxLifecycle

1.[app]主工程模块
2.[demo]测试用的，业务模块，业务模块可无限添加，每个业务模块都可独立运行，也可以作为主工程的依赖模块
3.[aidl] Android接口定义语言，为了实现进程间通信
4.[aliPlayer] 阿里播放器,可播放视频，音频，直播内容
5.[eosChain] 封装各种EOS链操作
6.[]

功能：
1. 框架支持mvp/mvvm 
2. 项目划分成主工程模块，业务模块，框架模块，功能模块,支持业务模块独立运行，方便多人独立开发
3. 使用androidx.startup 每个模块独立初始化
4. 使用ARouter直接跨模块跳转，传递数据
5. 使用dagger2注入present以及工具
6. 使用databinding 绑定数据
7. 使用easy_apt将页面和dagger自动绑定
8. 使用rxjava处理业务以及切换线程管理线程
9. 支持app不关闭页面自动换肤，换字体，切换语言
10.使用注解方式管理SharePreferences，支持多模块使用
11.可使用mmkv替换SharePreferences，提高读取效率
12.使用leanback查内存泄漏
13.利用AOP功能来实现方法耗时统计，暴力点击问题等
14.使用liveData来监听网络变化，屏幕变化
15.未捕获异常处理


