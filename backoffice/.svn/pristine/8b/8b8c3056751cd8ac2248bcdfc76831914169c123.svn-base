import {Injectable} from "@angular/core";
import {Response} from "@angular/http";

import "rxjs/add/operator/map";
import {CustomHttp} from "../../components/customhttp";
import {Common} from "../../common/common";

@Injectable()
export class TableWinLostService {

  private url = Common.URL + '/tableWinLoseReport/search';

  constructor(private http: CustomHttp) {
  }

  public search(starttime: String, endtime: String , tid: String, bid: String,  currentPage: number, pageSize: number) {
    let data = {
      'startTime': starttime,
      'endTime': endtime,
      'tableId': tid,
      'bootsId': bid,
      'currentPage': currentPage,
      'pageSize': pageSize
    }

    return this.http.post(this.url, data).map((res: Response) => res.json());
  }

  //格式化金钱
  public formatMoney(s, type) {
    var fs = false;
    if (s < 0) {
      s = s*-1;
      fs = true;
    }
    if (/[^0-9\.]/.test(s))
      return "0";
    if (s == null || s == "")
      return "0";
    s = s.toString().replace(/^(\d*)$/, "$1.");
    s = (s + "00").replace(/(\d*\.\d\d)\d*/, "$1");
    s = s.replace(".", ",");
    var re = /(\d)(\d{3},)/;
    while (re.test(s))
      s = s.replace(re, "$1,$2");
    s = s.replace(/,(\d\d)$/, ".$1");
    if (type == 0) {// 不带小数位(默认是有小数位)
      var a = s.split(".");
      if (a[1] == "00") {
        s = a[0];
      }
    }
    if (fs) {
      s = "-" + s;
    }
    return s;
  }


}
