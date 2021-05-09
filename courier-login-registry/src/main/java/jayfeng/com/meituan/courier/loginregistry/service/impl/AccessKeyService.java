package jayfeng.com.meituan.courier.loginregistry.service.impl;

import jayfeng.com.meituan.courier.loginregistry.constant.RabbitMQConstant;
import jayfeng.com.meituan.rpc.accesskey.bean.AccessKey;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 验证码密钥
 * @author JayFeng
 * @date 2021/4/29
 */
@Slf4j
@Service
public class AccessKeyService {

    @DubboReference(version = "1.0.2")
    private jayfeng.com.meituan.rpc.accesskey.service.RPCAccessKeyService rpcAccessKeyService;

    private static Map<Integer, List<AccessKey>> accessKeyMap = null;
    private static Random random = new Random();

    @PostConstruct
    private void initAccessKeyMap() {
        log.info("initAccessKeyMap 初始化密钥集合");
        accessKeyMap = rpcAccessKeyService.rpcGetAccessKeyMap();
    }

    /**
     * 随机获取一个密钥
     * @param useType 是用来做什么的：发送短信则类型为 0
     * @return 返回一个密钥
     */
    public AccessKey getAccessKey(Integer useType) {
        List<AccessKey> keyList = accessKeyMap.get(useType);
        log.info("getAccessKey 获取一个密钥 useType: {}", useType);
        if (keyList == null || keyList.isEmpty()) {
            log.info("getAccessKey 获取密钥失败, 密钥数据为空, useType: {}", useType);
            return null;
        }
        AccessKey accessKey = keyList.get(random.nextInt(keyList.size()));
        log.info("getAccessKey 获取到密钥 accessKey: {}", accessKey);
        return accessKey;
    }

    @RabbitListener(bindings = {
            @QueueBinding(
                    value = @Queue, // 创建临时队列
                    exchange = @Exchange(value = RabbitMQConstant.UPDATE_ACCESS_KEY_MSG_EXCHANGE, type = "fanout")
            )
    })
    private void receiveUpdateAccessKeyMsg(String message){
        log.info("receiveUpdateAccessKeyMsg 密钥更新消息接收 message: {}", message);
        initAccessKeyMap();
    }

}
