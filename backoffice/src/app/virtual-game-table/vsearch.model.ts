/**
 * Created by admin on 2017/9/24.
 */
export class SearchVGameTableForm {
  public gameTableId:string;
  public gameId:string;
  public status:string;
  public static create(model:SearchVGameTableForm){
    let temp = new SearchVGameTableForm();
    temp.gameTableId =model.gameTableId;
    temp.gameId =model.gameId;
    temp.status =model.status;
    return temp;
  }
}
