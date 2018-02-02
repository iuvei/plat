import {Component, OnDestroy, OnInit, ViewChild} from "@angular/core";
import "rxjs/add/operator/map";
import {ModalDirective} from "ng2-bootstrap/modal/modal.component";
import {CustomHttp} from "../components/customhttp";
import {TranslateService} from "@ngx-translate/core";
import {Common} from "../common/common";
import {ConfirmationService} from "primeng/components/common/api";
import {ToasterService} from "angular2-toaster";
import {SearchAccountRecord} from "./SearchAccountRecord";
import {TransRecordService} from "../report/transrecord/transrecord.service";
import {CommonUtil} from "../common/CommonUtil";

@Component({
  templateUrl: './account-record.component.html',
  styleUrls: ['./account-record.component.scss']
})
export class AccountRecordComponent implements OnInit,OnDestroy {
  private accountRecordList = [];
  private totalItems:number = 0;
  public maxSize:number = Common.MAX_PAGE_SIZE;
  private searchAccountRecord = new SearchAccountRecord();
  private betOrderDetail = {};

   //MAX_PAGE_SIZE = 25;

  @ViewChild('betOrderDetailDialog')
  private betOrderDetailDialog:ModalDirective;



  //public searchCondtion : Permission;
  constructor(private http:CustomHttp, private _translate: TranslateService, private _toaster: ToasterService,
              private confirmationService: ConfirmationService,private transRecordService:TransRecordService) { }

  ngOnInit() {
    let data = JSON.parse(sessionStorage.getItem("account-record.component"));
    if(data!=null){
      this.searchAccountRecord = SearchAccountRecord.create(data.searchAccountRecord);
      this.accountRecordList = data.accountRecordList;
      this.totalItems = data.totalItems;
      return;
    }


    this.searchAccountRecord.startTime = CommonUtil.getStartDate();
    this.searchAccountRecord.endTime = CommonUtil.getEndDate();

    // this.search();
  }

  ngOnDestroy(): void {
    let json = {"searchAccountRecord":this.searchAccountRecord,"accountRecordList":this.accountRecordList,"totalItems":this.totalItems};
    sessionStorage.setItem("account-record.component",JSON.stringify(json));
  }

  public pageChanged(event:any):void {
    this.searchAccountRecord.currentPage =  event.page;
    this.search();
  }

  search(){
    this.http.post(Common.URL+"/accountRecordReport/searchAccountRecord",this.searchAccountRecord)
      .map(res =>res.json())
      .subscribe(
        p => {
          this.accountRecordList = [];
          this.accountRecordList = p.data.data;
          this.totalItems = p.data.total;
        }
      );
  }



  findBetOrderDetail(orderId){
    this.http.get(Common.URL+"/accountRecordReport/betorder/"+orderId)
      .map(res =>res.json())
      .subscribe(
        p => {
          let data = p.data;
          data.roundResult = this.transRecordService.formatter1(data);
          this.betOrderDetail = data;
          this.betOrderDetailDialog.show();
        }
      );
  }
}
