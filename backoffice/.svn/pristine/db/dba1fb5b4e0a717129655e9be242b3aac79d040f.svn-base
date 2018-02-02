import {Injectable} from "@angular/core";
import {Response} from "@angular/http";

import "rxjs/add/operator/map";
import {CustomHttp} from "../../components/customhttp";
import {Common} from "../../common/common";

@Injectable()
export class LogRecordService {

  private url = Common.URL + '/loginLogReport/search';

  constructor(private http: CustomHttp) {
  }

  public search(beginDate: String, endDate: String,
                state: String, userName: String, ip: String, loginType: String,
                currentPage: number, pageSize: number) {
    let data = {
      'startTime': beginDate,
      'endTime': endDate,
      'status': state,
      'userName': userName,
      'ip': ip,
      'type': loginType,
      'currentPage': currentPage,
      'pageSize': pageSize
    };
    return this.http.post(this.url, data).map((res: Response) => res.json());
  }

}
