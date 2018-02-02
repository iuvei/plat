import {Injectable } from '@angular/core';
import { Http, Response, Headers } from '@angular/http';


import {Role} from "../model/role";

import 'rxjs/add/operator/map';
import {Common} from "../common/common";
import {CustomHttp} from "../components/customhttp";

@Injectable()
export class BetLevelService {

  private host = Common.URL;

  private url = this.host + '/betLevel/search';
  private addUrl = this.host + '/betLevel/create';
  private modifyUrl = this.host + '/betLevel/update';
  private deleteUrl = this.host + '/betLevel/delete';

  constructor(private http: CustomHttp) {
  }

  public search(data: any) {
    return this.http.post(this.url, data).map((res: Response) => res.json());
  }

  public create(data: any) {
    return this.http.post(this.addUrl, data).map((res: Response) => res.json());
  }

  public modify(data: any) {
    return this.http.post(this.modifyUrl, data).map((res: Response) => res.json());
  }

  public delete(data: any) {
    return this.http.post(this.deleteUrl, data).map((res: Response) => res.json());
  }

}
