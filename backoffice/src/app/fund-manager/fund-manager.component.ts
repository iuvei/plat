import {Component, OnInit, ViewChild} from "@angular/core";
import {TranslateService} from "@ngx-translate/core";
import {CustomHttp} from "../components/customhttp";
import {Common} from "../common/common";
import 'rxjs/add/operator/map';
import {Message} from "primeng/primeng";
import {ModalDirective} from "ng2-bootstrap";
import {ModifyBalanceRequest} from "./ModifyBalanceRequest";

@Component({
  templateUrl: './fund-manager.component.html'
})
export class FundManagerComponent implements  OnInit {
  private url = Common.URL+"/fundManager/";
  private balance = 0;
  private modifyBalance = new ModifyBalanceRequest();
  private msgs: Message[] = [];
  // 调整点数弹窗
  @ViewChild('modifyBalanceModal')
  public modifyBalanceModal: ModalDirective;

  constructor(private _translate: TranslateService,private http: CustomHttp) { }

  ngOnInit() {
    this.search();
  }

  public search() {
    this.http.get(this.url + "getOperateadminBalance")
      .map(res =>res.json())
      .subscribe(rec=>{
        if (rec.status==0){
          this.balance = rec.data;
        }else {
          this.showError(rec.msg);
        }
      });
  }

  public changeBalance(){
    this.http.post(this.url+"incomeOperateadmin",this.modifyBalance)
      .map(res=>res.json())
      .subscribe(rec=>{
        if(rec.status==0){
          this.showSuccess("");
          this.modifyBalanceModal.hide();
          this.search();
        }else {
          this.showError(rec.message);
        }
      })
  }

  showError(msg:string) {
    this.msgs = [];
    this.msgs.push({severity:'error', summary: this._translate.instant('common.error'), detail:msg});
  }

  showSuccess(msg:string) {
    this.msgs = [];
    this.msgs.push({severity:'success', summary: this._translate.instant('common.success'), detail:msg});
  }

  closeModifyBalanceModal() {
    this.modifyBalance = new ModifyBalanceRequest();
    this.modifyBalanceModal.hide();
  }

  openModifyBalanceModal() {
    this.modifyBalance = new ModifyBalanceRequest();
    this.modifyBalanceModal.show();
  }
}
