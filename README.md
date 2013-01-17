jvm-tool
========

现在暂时只有一个非常简单的class字节码解析程序.主要的用途是学习.有点类似javap的功能.

#2013-01-15
已经完成了magic,minor_version,major_version,constant_pool,access_flags,this_class,super_class,interfaces的解析

#2012-01-17
完成了field_info的解析..不过field的属性值解析了 ConstantValue 和Deprecated.其他的属性都未解析.

  
                               add by zhenghui
