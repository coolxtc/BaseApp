app基础框架

该框架基于组件化思想搭建，分为以下几个主要模块
1.app->组件组合处
2.base->封装了基础的通用业务，如网络请求，路由跳转，消息通讯及一些基类等等
3.business->各业务组件存放处（原则上来说module-xx实现业务，module-xx-interface实现该module需要对外暴露的接口或消息通知）

使用方法：

1.项目的gradle.properties文件中，有自定义属性isModule，通过该值可实现组件化开关

2.若需创建新的组件，因按照以下步骤进行
    a.在business目录下创建新的Module，以module-xx结构命名

    b.在module的src-main目录下，新建module文件夹，创建AndroidManifest.xml

    c.在module的build.gradle加入下列代码，用来控制组件化
        //通过标识符判断是否开启组件化
        if (isModule.toBoolean()) {
          apply plugin: 'com.android.application'
        } else {
          apply plugin: 'com.android.library'
        }
        //启用kapt
        apply plugin: 'kotlin-kapt'
        //通过标识符选择使用哪个Manifest文件
        android {
            ...
            sourceSets {
                main {
                    if (isModule.toBoolean()) {
                        manifest.srcFile 'src/main/module/AndroidManifest.xml'
                    } else {
                        manifest.srcFile 'src/main/AndroidManifest.xml'
                    }
                }
            }
        }
        //ARouter路由配置
        kapt {
            arguments {
                arg("AROUTER_MODULE_NAME", project.getName())
            }
        }
        //引入ARouter-compiler和common库
        dependencies {
            ...
            //ARouter
            kapt 'com.alibaba:arouter-compiler:1.2.2'
            implementation project(':base:common')
        }

    d.在business目录下创建新的Module，以module-xx-interface结构命名

    e.在base:common库的gradle文件中，添加以下代码
        //引入暴露接口
        dependencies {
            ...
            api project(':business:module-xx-interface')
        }

    f.在app的gradle文件中，添加以下代码
        dependencies {
            ...
            if (!isModule.toBoolean()) {
                ...
                implementation project(':business:module-xx')
            }
        }

tips：各组件的使用说明请进入组件内部的readme文件查看

项目中使用的三方框架文档
渠道包walle:
https://github.com/Meituan-Dianping/walle
ARouter:
https://github.com/alibaba/ARouter/blob/master/README_CN.md