/**
 * Created by sunny on 2017/4/8 0008.
 */
export class SearchModel{
  public loginName : string;
  public startTime:Date=new Date();
  public endTime:Date =new Date() ;
  public page : number;
  public rows : number;
  public pageSize = 20;

  public static create(model:SearchModel){
    let temp = new SearchModel();
    temp.page = model.page;
    temp.pageSize = model.pageSize;
    temp.startTime = new Date(model.startTime);
    temp.endTime = new Date(model.endTime);
    temp.loginName = model.loginName;
    temp.rows = model.rows;
    return temp;
  }
}
