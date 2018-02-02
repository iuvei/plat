import {Injectable} from "@angular/core";
import {Response} from "@angular/http";

import "rxjs/add/operator/map";
import {Common} from "../common/common";
import {CustomHttp} from "../components/customhttp";

@Injectable()
export class ApiLogService {

  private host = Common.URL;

  private url = this.host + '/apiLog/search';


  constructor(private http: CustomHttp) {
  }

  public search(beginDate:String, endDate:String , merchantNo:String, ip:String,status:number,currentPage: number, pageSize: number) {
    let data = {
      'startTime': beginDate,
      'endTime': endDate,
      'merchantNo': merchantNo,
      'ip': ip,
      'status': status,
      'currentPage': currentPage,
      'pageSize': pageSize
    };
    return this.http.post(this.url, data).map((res: Response) => res.json());
  }

}
