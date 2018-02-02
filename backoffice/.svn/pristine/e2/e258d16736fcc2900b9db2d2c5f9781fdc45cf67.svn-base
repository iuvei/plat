import {Component, OnDestroy, OnInit} from "@angular/core";
import {CustomHttp} from "../components/customhttp";
import {SearchModel} from "./search.model";
import {Common} from "../common/common";
import {CommonUtil} from "app/common/CommonUtil";
import {TranslateService} from "@ngx-translate/core";

@Component({
  selector: 'dealer-class-record',
  templateUrl: './dealer-class-record.component.html',
  styleUrls: ['./dealer-class-record.component.scss']
})
export class DealerClassRecordComponent implements OnInit,OnDestroy {
  private searchUrl = Common.URL+"/dealerClassRecord/search";
  private searchModel : SearchModel = new SearchModel();

  public currentPage: number = 1;

  public recordList:any=[];

  public smallnumPages: number = 1;
  public totalItems: number = 0;
  public pageSize: number = 20;
  public maxSize: number = Common.MAX_PAGE_SIZE;

  constructor(private http:CustomHttp,private _translate: TranslateService) {
  }

  ngOnInit() {
    let obj = JSON.parse(sessionStorage.getItem("DealerClassRecord-component"));
    if(obj){
      this.searchModel = SearchModel.create(obj.searchModel);
      this.recordList = obj.recordList;
    }else {
      this.searchModel.startTime = CommonUtil.getStartDate();
      this.searchModel.endTime = CommonUtil.getEndDate();
      this.search();
    }
  }

  ngOnDestroy(): void {
    let json = {"searchModel":this.searchModel,"recordList":this.recordList};
    sessionStorage.setItem("DealerClassRecord-component",JSON.stringify(json));
  }

  public pageChanged(event:any):void {
    this.currentPage = event.page;
    this.searchModel.page =  event.page;
    this.search();
  }


  search(){
    let data = {
      'startTime':this.searchModel.startTime!=null?CommonUtil.formatDate(this.searchModel.startTime):this.searchModel.startTime,
      'endTime': this.searchModel.endTime!=null?CommonUtil.formatDate(this.searchModel.endTime):this.searchModel.endTime,
      'loginName': this.searchModel.loginName,
      'currentPage': this.searchModel.page,
      'pageSize': this.searchModel.pageSize
    }
    this.http.post(this.searchUrl,data)
      .map(res =>res.json())
      .subscribe(rec=>{
          this.recordList = rec.data.data;
          this.totalItems = rec.data.total;
      });
  }
}
