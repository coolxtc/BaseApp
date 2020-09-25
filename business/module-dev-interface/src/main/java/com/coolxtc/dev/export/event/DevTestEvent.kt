package com.coolxtc.dev.export.event

/**
 * Desc:
 * 测试 LiveEventBus
 * １.新建一个消息类
 * ２.需要发送通知的地方调用　LiveEventBusUtil 的　postEvent()　函数
 * ３.需要接收消息的地方调用　LiveEventBusUtil　的　observeEvent() 函数(因为 LiveEventBus 的特性不需要反注册)
 *
 * @author xtc
 * @date 2020/9/3
 * @email qsawer888@126.com
 */
class DevTestEvent {
    var msg = ""
}