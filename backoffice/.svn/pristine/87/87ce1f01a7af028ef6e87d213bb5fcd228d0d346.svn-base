import {Injectable} from "@angular/core";
import {Response} from "@angular/http";

import "rxjs/add/operator/map";
import {CustomHttp} from "../../components/customhttp";
import {Common} from "../../common/common";
import {TranslateService} from "@ngx-translate/core";
import {Dict} from "../../model/dict";
import {SearchModel} from "./search.model";
import {isUndefined} from "util";
import {CommonUtil} from "../../common/CommonUtil";

@Injectable()
export class RoomMemberService {

  private url = '/roomMemberReport/search';
  private dicts ;

  constructor(private http: CustomHttp, private _translate: TranslateService) {
    this.dicts = JSON.parse(localStorage['loginUser']).dicts as Dict[];
  }

  public search(searchModel: SearchModel) {
    let data = {
      'startTime': CommonUtil.formatDate(searchModel.startTime),
      'endTime': CommonUtil.formatDate(searchModel.endTime),
      'loginName': searchModel.loginName,
      'currentPage': searchModel.currentPage,
      'pageSize': searchModel.pageSize
    }

    return this.http.post(Common.URL + this.url, data).map((res: Response) => res.json());
  }
}
