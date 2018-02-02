export class AccountModalConfig {
  // 称号
  title: boolean = false;
  // 上级限制点数
  balance: number = 0;
  // 默认是否展开限红树
  isExpandedLimitData: boolean = false;
  // 洗码比
  washCode: boolean = false;
  // spLimit   spercentage
  washCodeMaxPercent: string = '100';
  // VIP包台
  vipPackage: boolean = false;
  // 代理占成
  agentHold: boolean = false;
  // opLimit  opercentage
  agentHoldMaxPercent: string = '100';
  // VIP包台占成
  vipPackageHold: boolean = false;
  // copLimit  copercentage
  vipPackageHoldMaxPercent: string = '100';
  // 最高赢额
  maxWin: boolean = false;

  // 语音
  voice: boolean = false;
  // 语音洗码比
  voiceWashCode: boolean = true;
  // yspLimit  yspercentage
  voiceWashCodeMaxPercent: string = '100';
  // 语音占成
  voiceHold: boolean = false;
  // yopLimit  yopercentage
  voiceHoldMaxPercent: string = '100';

  // 老虎机
  tiger: boolean = false;
  // 老虎机洗码比
  tigerWashCode: boolean = false;
  // tigerspLimit  tigerspercentage
  tigerWashMaxPercent: string = '100';
  // 老虎机占成
  tigerHold: boolean = false;
  // tigeropLimit  tigeropercentage
  tigerHoldMaxPercent: string = '100';
  // 充值金额
  tigerRechargeMoney: boolean = false;

  // 彩票
  ticket: boolean = false;
  // 彩票洗码比
  ticketWashCode: boolean = false;
  // ticketspLimit  ticketspercentage
  ticketWashCodeMaxPercent: string = '100';
  // 彩票占成
  ticketHold: boolean = false;
  // ticketopLimit  ticketopercentage
  ticketHoldMaxPercent: string = '100';
  // 限额
  ticketLimit: boolean = false;

  constructor() {}
}
