/**
 * 商户模型。
 * Created by sunny on 2017/4/6 0006.
 */
export class UserBusiness {
  /**商户ID*/
  public id: number;
  /**上级ID*/
  public parentId: number;
  /**类型*/
  public type: number;
  /**来源*/
  public source: number;
  /**商户号*/
  public number: string;
  /**商户标识*/
  public buhdr: string;
  /**商户描述*/
  public remark: string;
  /**商户秘钥*/
  public privateKey: string;
  public createby: string;
  /**创建时间*/
  public createTime: string;
  public updateby: string;
  /**修改时间*/
  public updatedatetime: number;
  /**单笔充值上限*/
  public maxdepositlimit: number;
  public lev: number;
  public returnurl: string;
  public ips: string;
  public language: string;
  public isusebalance: boolean;
  public isaddbu: boolean;
  public balance: number;
  /**状态*/
  public status: number;
  public state: string;
  public _parentId: number;
  public isedit: number;
  public isadd: number;
  public flev: number;
  /**关联代理号*/
  public agentname: string;
  /**可用额度*/
  public limitstatus: number ;

  /**额度控制*/
  public ctrlamount: number;
  /**限红*/
  public chips: string;
}
