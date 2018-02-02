import {Injectable } from '@angular/core';
import { Http, Response, Headers,RequestOptions } from '@angular/http';

import 'rxjs/add/operator/map';
import {Common} from "../common/common";
import {CustomHttp} from "../components/customhttp";

@Injectable()
export class AbnormalTableService {

  private host = Common.URL;

  private url = this.host + '/abnormalTableManage/search';
  private searchRoundUrl = this.host + '/abnormalTableManage/searchRound';
  private correctDataUrl = this.host + '/abnormalTableManage/correctData';
  private settlementUrl = this.host + '/abnormalTableManage/settlement';
  private refundUrl = this.host + '/abnormalTableManage/refund';

  private getGameTypeUrl = this.host + '/gametable/getGameType';
  private options = new RequestOptions({});
  constructor(private http: CustomHttp) {
  }

  search(data: any) {
    this.options.headers = new Headers();
    this.options.headers.append('Content-Type', 'application/x-www-form-urlencoded');
    return this.http.post(this.url, this.transformRequest(data),this.options).map((res: Response) => res.json());
  }

  searchRound(data: any) {
    this.options.headers = new Headers();
    this.options.headers.append('Content-Type', 'application/x-www-form-urlencoded');
    return this.http.post(this.searchRoundUrl, this.transformRequest(data),this.options).map((res: Response) => res.json());
  }

  correctData(data: any) {
    return this.http.post(this.correctDataUrl, data).map((res: Response) => res.json());
  }

  settlement(data: any) {
    return this.http.post(this.settlementUrl, data).map((res: Response) => res.json());
  }

  refund(data: any) {
    return this.http.post(this.refundUrl, data).map((res: Response) => res.json());
  }

  getGameType() {
    return this.http.post(this.getGameTypeUrl, null).map((res: Response) => res.json());
  }
  public transformRequest(data:any){
    var str =[];
    for(var p in data){
      if(data[p] == null || data[p]=='null'){
        continue;
      }
      str.push(p+'='+data[p]);
    }
    return str.join('&');
  }

}
