package cn.aszswaz;

import cn.aszswaz.smtp.Receive;
import lombok.extern.slf4j.Slf4j;

/**
 * 邮箱服务器
 */
@Slf4j
public class Mail {
    public static void main(String[] args) {
        try {
            Constant.init();
            Receive.start();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
