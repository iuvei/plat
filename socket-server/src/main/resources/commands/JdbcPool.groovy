package commands

import com.alibaba.druid.pool.DruidDataSource
import com.na.user.socketserver.common.SpringContextUtil
import org.crsh.cli.Command
import org.crsh.cli.Usage
import org.crsh.command.InvocationContext
import org.crsh.text.ui.UIBuilder

/**
 * Created by Administrator on 2017/5/18 0018.
 */
@Usage("查询jdbc连接池工作情况")
class JdbcPool {
    @Usage("连接")
    @Command
    def info(InvocationContext context) {
        DruidDataSource dataSource = SpringContextUtil.getBean(DruidDataSource.class);
        Map<String,Object> res = new HashMap<>();
        res.put("activeCount",dataSource.getActiveCount());
        res.put("maxActive",dataSource.getMaxActive());
        dataSource.getActivePeakTime()
        return res;
    }

}
