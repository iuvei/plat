import {Injectable} from "@angular/core";
import {Response} from "@angular/http";
import "rxjs/add/operator/map";
import {CustomHttp} from "../../components/customhttp";
import {Common} from "../../common/common";
import {TranslateService} from "@ngx-translate/core";
import {Dict} from "../../model/dict";
import {CommonUtil} from "../../common/CommonUtil";

@Injectable()
export class RoomHedgeService {

  private realUrl = '/roomHedgeReport/search';
  private viewUrl = '/roomHedgeReport/view';
  private dicts ;

  constructor(private http: CustomHttp, private _translate: TranslateService) {
    this.dicts = JSON.parse(localStorage['loginUser']).dicts as Dict[];
  }

  public search(startTime:Date,endTime:Date,agentName:string,roomAgent:string,roomNumber:string,statisType:number) {
    let data = {
      'startTime': CommonUtil.formatDate(startTime),
      'endTime': CommonUtil.formatDate(endTime),
      'agentName': agentName,
      'roomAgent': roomAgent,
      "roomNumber":roomNumber,
      "statisType":statisType
    }
    return this.http.post(Common.URL + this.realUrl, data).map((res: Response) => res.json());
  }
  public view(startTime:Date,endTime:Date,agentName:string,tableId:number,roundId:number) {
    let data = {
      'startTime': CommonUtil.formatDate(startTime),
      'endTime': CommonUtil.formatDate(endTime),
      'tableId': tableId,
      "roundId":roundId,
      "agentName":agentName
    }
    return this.http.post(Common.URL + this.viewUrl, data).map((res: Response) => res.json());
  }

  //处理后台返回的数据
  public dealListData(datas:any[]) {
    if (datas.length <= 0)
      return datas;
    let rows:any[] =[];
    for (let data of datas) {
      data.gameName = data.gameTable + this._translate.instant('common.table')+ data.bootsNum + this._translate.instant('common.boots') + data.roundNum + this._translate.instant('common.round');
      if(data.itemId ==101 || data.itemId ==102){ //显示庄、闲投注
        rows.push(data);
      }
    }
    return rows;
  }
}
