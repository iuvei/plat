import {Injectable} from "@angular/core";
import {Response} from "@angular/http";

import "rxjs/add/operator/map";
import {CustomHttp} from "../../components/customhttp";
import {Common} from "../../common/common";

@Injectable()
export class WinLostService {

  private url = Common.URL + '/agentReport/queryAgentWinLostReport';

  constructor(private http: CustomHttp) {
  }

  public search(beginDate: String, endDate: String ,type:number,userId:number) {
    let data={
      'startTime':beginDate,
      'endTime':endDate,
      'type':type,
      'userId':userId
    };

    return this.http.post(this.url, data).map((res: Response) => res.json());
  }

}
