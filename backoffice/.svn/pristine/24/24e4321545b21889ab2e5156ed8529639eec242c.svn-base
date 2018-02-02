import {Injectable } from '@angular/core';
import { Http, Response, Headers } from '@angular/http';

import 'rxjs/add/operator/map';
import {CustomHttp} from "../components/customhttp";
import {Common} from "../common/common";

@Injectable()
export class BetService {
  private  host = Common.URL;
  private url = this.host+'/livebet/';

  private headers: Headers;

  constructor(private http: CustomHttp) {
  }

  public search() {
    return this.http.get(this.url+"search").map((res: Response) => res.json());
  }

  public getTopBet(roundId) {
    return this.http.get(this.url+"getTopBet/"+roundId).map((res:Response)=>res.json());
  }

}
