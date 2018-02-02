/**
 * Created by Administrator on 2017/4/1 0001.
 */
export class User{
  public uid : number;
  public loginName : string;
  public password : string;
  public nickName : string;
  public balance = 0;
  public washBalance = 0; //洗码金额
  public tigerLimit = 0;
  public slotBalance = 0;
  public ticketLimit = 0;
  public returnURL : string;
  public rePassword : string;
  /**
   * 用户状态 0正常1冻结2删除
   */
  public status = 0;

  public isBet = 0;

  /**
   * 等级，8是用户
   */
  public levelId : number;
  /**
   * 上线ID,0是直接对公司。无代理
   */
  public superiorId : number;
  /**
   * 上线ID,最大直级
   */
  public superId : number;
  public parentPath : number;

  /**
   * 条形码
   */
  public barrcode : number;
  /**
   * 用户类型 1:系统用户2:荷官3:普通游戏用户4电视虚拟用户5:代理
   * 6:代理子账号7:包台游戏用户8语言用户9荷官主管10语音投注员
   * 11 商户代理12老虎机13彩票14体育
   */
  public typeId : number;
  /**
   * 用户来源 1：代理网2现金网,0：无类型(不可以登录前端游戏)
   */
  public source : number;
  public lastLoginIP : string;
  public lastLoginDate : Date;
  public lastLogoutDate : Date;

  /**
   * 玩法类型 1:包台2:语音
   */
  public GameType : number;

  /**
   * 非真人游戏类型：1（老虎机）2（彩票）3（体育）12（老虎机、彩票）13（老虎机、体育）23（彩票、体育）123（老虎机、彩票、体育）
   */
  public unLiveType : number;

  /**
   * logoUrl
   */
  public banner : string;

  /**
   * 样式文本
   */
  public csstext : string;
  public startTime : string;
  public endTime : string;

}
