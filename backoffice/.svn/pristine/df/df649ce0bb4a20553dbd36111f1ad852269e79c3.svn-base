/**
 * Created by Administrator on 2017/7/11 0011.
 */
export class SearchModel{
  public currentPage: number = 1;
  public pageSize: number = 20;

  //quert param
  public startTime: Date = new Date();
  public endTime: Date = new Date();
  public state: string = '';
  public loginType: string = '-1';
  public userName: string = '';
  public ip: string = '';

  public static create(model:SearchModel){
    let temp = new SearchModel();
    temp.currentPage = model.currentPage;
    temp.pageSize = model.pageSize;
    temp.startTime = new Date(model.startTime);
    temp.endTime = new Date(model.endTime);
    temp.state = model.state;
    temp.loginType = model.loginType;
    temp.userName = model.userName;
    temp.ip = model.ip;
    return temp;
  }
}
