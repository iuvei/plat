///<reference path="../components/customhttp.ts"/>
import {Common} from "../common/common";
import {Response} from "@angular/http";
import {CustomHttp} from "../components/customhttp";
import {Injectable} from "@angular/core";

@Injectable()
export class MonitorSerice{

  private host = Common.URL;
  private queryUrl = this.host + '/serverMonitor/search';
  private monitorUrl = this.host + '/serverMonitor/';
  private modifyUrl = this.host + '/serverMonitor/modify/';


  constructor(private http: CustomHttp) {
  }

  search(data: any) {
    return this.http.post(this.queryUrl, data).map((res: Response) => res.json());
  }

  view(requestMethod: string,data:any) {
    return this.http.post(this.monitorUrl + requestMethod, data).map((res: Response) => res.json());
  }

  modify(status:string) {
    return this.http.post(this.modifyUrl+status, null).map((res: Response) => res.json());
  }

}
