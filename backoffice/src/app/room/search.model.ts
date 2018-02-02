/**
 * Created by admin on 2017/9/24.
 */
export class SearchRoomForm {
  public roomName:string = '';
  public createUser:string = '';
  public searchRoomStatus:string='';
  public type:number;
  public startTime:Date;
  public endTime:Date;

  public static create(model:SearchRoomForm){
    let temp = new SearchRoomForm();
    temp.roomName =model.roomName;
    temp.searchRoomStatus =model.searchRoomStatus;
    temp.type =model.type;
    temp.startTime = new Date(model.startTime);
    temp.endTime = new Date(model.endTime);
    return temp;
  }
}
