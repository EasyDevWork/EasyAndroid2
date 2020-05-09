环境配置参考：https://www.jianshu.com/p/b4431ac22ec2  建议使用Cmake
ndk使用参考：https://blog.csdn.net/qq_21556263/article/details/84144396
ndk教程参考：https://blog.csdn.net/afei__/article/details/99976388

耗时统计使用方法
#include "TimeUtils.h" // 包含这个头文件

int dump_image(const char *path, unsigned char *data, unsigned int len) {
    FILE *file = fopen(path, "wb");
    if (file == NULL) {
        LOGE("fopen %s failed.", path);
        return -1;
    }
	__TIC1__(fwrite) // 耗时统计起始处
    int size = fwrite(data, 1, len, file);
    __TOC1__(fwrite) // 耗时统计终止处，注意括号内的内容必须一致

    fclose(file);
    return 0;
}

