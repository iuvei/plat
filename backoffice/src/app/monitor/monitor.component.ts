import {Component, OnInit, ViewChild} from "@angular/core";
import {TranslateService} from "@ngx-translate/core";
import {CustomHttp} from "../components/customhttp";
import {ModalDirective} from "ng2-bootstrap/modal/modal.component";
import {ConfirmationService} from "primeng/components/common/api";
import {ToasterService} from "angular2-toaster/angular2-toaster";
import {Common} from "../common/common";
import {MonitorSerice} from "./monitor.service";
import {MonitorForm} from "./monitorform";

@Component({
  templateUrl: 'monitor.component.html',
  styleUrls: ['./monitor.component.scss']
})
export class MonitorComponent implements  OnInit {

  @ViewChild('envModal')
  public envModal:ModalDirective;

  @ViewChild('healthModal')
  public healthModal: ModalDirective;

  public currentPage: number = 1;
  public smallnumPages: number = 1;
  public totalItems: number = 0;
  public pageSize: number = 20;
  public maxSize: number = Common.MAX_PAGE_SIZE;

  public datalist:any[] = [];

  public flag :number;

  public monitorForm: MonitorForm = new MonitorForm();

  // 当前登录用户
  currentUser: any;

  constructor(private _monitorSerice:MonitorSerice,private _translate: TranslateService,private http: CustomHttp,private confirmDialog: ConfirmationService, private toaster:ToasterService) {
  }

  ngOnInit() {
    const user = JSON.parse(localStorage.getItem('loginUser'));
    this.currentUser = user.user;
    this.search();
  }

  public pageChanged(event: any): void {
    this.currentPage =  event.page;
    this.search();
  }

  public search() {
    let condition = {
      currentPage: this.currentPage,
      pageSize: this.pageSize
    };
    this._monitorSerice.search(condition).subscribe(rec=>{
      this.datalist = rec.data.data;
      this.flag = rec.data.flag;
    });
  }

  public checkHealth(item:any){
    this._monitorSerice.view("health",JSON.stringify(item)).subscribe(rec=>{
      this.monitorForm.health =rec.data;
      this.healthModal.show();
    });
  }

  public checkEvn(item:any){
    this._monitorSerice.view("env",JSON.stringify(item)).subscribe(rec=>{
      this.monitorForm.env =rec.data;
      this.envModal.show();
    });
  }

  public operateMonitor(status:string){
    let message = '';
    if(status =='1'){
      message=this._translate.instant('monitor.startConfirm');
    }else{
        message=this._translate.instant('monitor.stopConfirm');
    }
    this.confirmDialog.confirm({
      message: message,
      accept : () => {
        this._monitorSerice.modify(status).subscribe(data => {
          this.flag = data.data;
          if (data.status === 0) {
            this.toaster.pop('success', this._translate.instant('common.success'), data.msg);
          } else {
            this.toaster.pop('error', this._translate.instant('common.error'), data.msg);
          }
        });
      }
    });
  }

  public closeHealthModal(){
    this.healthModal.hide();
  }

  public closeEnvModal(){
    this.envModal.hide();
  }
}
