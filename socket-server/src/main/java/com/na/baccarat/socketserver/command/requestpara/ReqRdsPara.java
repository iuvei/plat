package com.na.baccarat.socketserver.command.requestpara;

import com.na.user.socketserver.command.requestpara.CommandReqestPara;


/**
 * body:{ {"gid":游戏ID, "tid":台桌ID, "stu":桌子状态,did:"荷官ID",dna:"荷官昵称",kind:"桌子的类别(普通，VIP，龙湖)"
 * "rds":[{rid:"局ID","rn":"局数",ret:"结果"},{rid:"局ID","rn":"局数",ret:"结果"}....]}
 * 
 * ,{"gid":游戏ID, "tid":台桌ID, "stu":桌子状态,did:"荷官ID",dna:"荷官昵称"
 * "rds":[{rid:"局ID","rn":"局数",ret:"结果"},{rid:"局ID","rn":"局数",ret:"结果"}....]},
 * 
 * {"gid":游戏ID, "tid":台桌ID, "stu":桌子状态,tp:'',did:"荷官ID",dna:"荷官昵称"
 * "rds":[{rid:"局ID","rn":"局数",ret:"结果"},{rid:"局ID","rn":"局数",ret:"结果"}....]} 
 * @author Administrator
 *
 */
public class ReqRdsPara extends CommandReqestPara {
	
	
	 
}
