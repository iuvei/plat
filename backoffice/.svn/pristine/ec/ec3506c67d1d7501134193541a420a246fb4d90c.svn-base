import {Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';

import 'rxjs/add/operator/map';
import {Common} from "../common/common";
import {CustomHttp} from "../components/customhttp";

@Injectable()
export class IpBlackListService {

  private host = Common.URL;

  private url = this.host + '/ipBlackWhiteList/search';
  private createUrl = this.host + '/ipBlackWhiteList/create';
  private batchCreateUrl = this.host + '/ipBlackWhiteList/batchAdd';
  private removeUrl = this.host + '/ipBlackWhiteList/delete';

  constructor(private http: CustomHttp) {
  }

  search(data: any) {
    return this.http.post(this.url, data).map((res: Response) => res.json());
  }

  create(data: any) {
    return this.http.post(this.createUrl, data).map((res: Response) => res.json());
  }

  batchCreate(data: any) {
    return this.http.post(this.batchCreateUrl, data).map((res: Response) => res.json());
  }

  remove(data: any) {
    return this.http.post(this.removeUrl, data).map((res: Response) => res.json());
  }

}
