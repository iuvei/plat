/**
 * Created by sunny on 2017/4/9 0009.
 */
export class CreateBuModel {

  public id: number;
  /**
   * 商户描述
   */
  public remark: string;
  /**
   * 商户标识
   */
  public merchantPrefix: string;
  public balance: number;
  /**
   * 商户类型
   */
  public merchantType: number;
  public userType: number;
  public returnURL: string;
  public allowIpList: string;
  public chips: any;
  // 商户号
  public number: string;
  public type: string;
  public parentId: number;
  public intoPercentage:number;
  public oldIntoPercentage:number;
  public agentHoldMaxPercent:number;
  public roomMember:number;
}
