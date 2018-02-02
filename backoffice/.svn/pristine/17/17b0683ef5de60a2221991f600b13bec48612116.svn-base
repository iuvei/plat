import {Component, OnInit, ViewChild} from "@angular/core";
import {Permission} from "../model/permission";
import "rxjs/add/operator/map";
import {Http} from "@angular/http";
import {SearchPermission} from "./SearchPermission";
import {ModalDirective} from "ng2-bootstrap/modal/modal.component";
import {CustomHttp} from "../components/customhttp";
import {PubSubService} from "../components/pubsup.service";
import {TranslateService} from "@ngx-translate/core";
import {Common} from "../common/common";
import {ConfirmationService} from "primeng/components/common/api";
import {ToasterService} from "angular2-toaster";

@Component({
  selector: 'app-permission',
  templateUrl: './permission.component.html',
  styleUrls: ['./permission.component.scss']
})
export class PermissionComponent implements OnInit {
  public permissionList : Permission[] = [];
  public totalItems:number = 0;
  public maxSize: number = Common.MAX_PAGE_SIZE;
  public searchPermission = new SearchPermission();
  public modifyPermission  = new Permission();
  @ViewChild('modifyModal')
  public modifyModal:ModalDirective;

  public statusList = [{"name": this._translate.instant('common.all'), "value":null},{"name": this._translate.instant('common.enable'), "value":1,'asValue':true},{"name": this._translate.instant('common.disable'), "value":0,'asValue':false}];


  //public searchCondtion : Permission;
  constructor(private http:CustomHttp, private _translate: TranslateService, private _toaster: ToasterService,
              private confirmationService: ConfirmationService) { }

  ngOnInit() {
    this.search();
  }


  public pageChanged(event:any):void {
    this.searchPermission.curPage =  event.page;
    this.search();
  }

  search(){
    this.http.post(Common.URL+"/sysPermissionFunction/searchPermission",this.searchPermission)
      .map(res =>res.json())
      .subscribe(
        p => {
          this.permissionList = [];
          let temps = p['rows'];
          for(let i=0;i<temps.length;i++){
            let item = temps[i];
            let per = new Permission();
            per.permissionID = item['permissionid'];
            per.permissionName = item['permissionname'];
            per.permissionDesc = item['permissiondesc'];
            per.permissionOrder = item['permissionorder'];
            per.permissionUrl = item['permissionurl'];
            per.groupID = item['groupid'];
            per.status = item['status'];
            this.permissionList.push(per);
          }
          this.totalItems = p['total'];
        }
      );
  }

  initModifyDialog(item){
    this.modifyPermission = item;
    this.modifyModal.show();
  }

  updatePermission(per : Permission) {
    this.http.post(Common.URL+"/sysPermissionFunction/updatePermission",per)
      .map(res => res.json())
      .subscribe(r =>{
        if (r.ok){
          this.search();
          this.modifyModal.hide();
        }else {
          this._toaster.pop('error', this._translate.instant('common.error'), r.msg);
        }
      });
  }

  updatePermissionStatus(id,status){
    this.confirmationService.confirm({
      message: status ==1?this._translate.instant('permission.confirmEnablePermStatus'):this._translate.instant('permission.confirmDisablePermStatus'),
      accept : () => {
        let per = new Permission();
        per.permissionID = id;
        per.statusInt = status;
        this.updatePermission(per);
      }
    });

  }
}
