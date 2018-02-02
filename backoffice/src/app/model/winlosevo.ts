/**
 * Created by Brent on 2017/5/14.
 */


export class WinloseVo{

  /**
   * 统计类型
   */
  public statisType:number;
  /**
   * 统计统计类型描述
   */
  public statisTypeDesc:string;

  /**
   * 代理ID
   */
  public agentId:number;

  /**
   * 账号
   */
  public loginName:string;

  /**
   * 路径深度
   */
  public parentPath:string;

  /**
   * 交易次数
   */
  public tradeTime:number;
  /**
   * 投注金额
   */
  public amountBetting:number;
  /**
   * 输赢金额
   */
  public winLostAmount:number;
  /**
   * 洗码量
   */
  public  washBetting:number;
  /**
   * 洗码比
   */
  public  washPercentage:string;
  /**
   * 洗码金额
   */
  public  washAmount:number;
  /**
   * 代理总收入
   */
  public  agentIncome:number;
  /**
   * 代理占成比
   */
  public  intoPercentage:string;
  /**
   * 代理交公司收入
   */
  public  agentCompanyIncome:string;
  /**
   * 公司获利比
   */
  public  winloSepercentage:number;


}
