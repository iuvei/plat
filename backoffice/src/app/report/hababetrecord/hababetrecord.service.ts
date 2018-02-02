import {Injectable } from '@angular/core';
import {Response} from '@angular/http';

import 'rxjs/add/operator/map';
import {Common} from "../../common/common";
import {CustomHttp} from "../../components/customhttp";

@Injectable()
export class HabaBetRecordService {

  private url = Common.URL + '/habaBetRecord/search';

  constructor(private http: CustomHttp) {
  }

  public search(data: any) {
    return this.http.post(this.url, data).map((res: Response) => res.json());
  }


}
