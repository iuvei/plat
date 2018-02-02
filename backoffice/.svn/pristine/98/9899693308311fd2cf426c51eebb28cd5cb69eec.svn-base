import {
  Component,
  ComponentFactoryResolver,
  ComponentRef,
  OnInit,
  ViewChild,
  ViewContainerRef,
  ViewRef
} from "@angular/core";
import {Permission} from "../model/permission";
import "rxjs/add/operator/map";
import {CustomHttp} from "../components/customhttp";
import {UserBusiness} from "../model/userbusiness";
import {BusinessTableComponent} from "./business-table.component";
import {ToasterService} from "angular2-toaster";
import {Common} from "../common/common";
import {isUndefined} from "util";

@Component({
  templateUrl: './business-manage.component.html',
  styleUrls: ['./business-manage.component.scss']
})
export class BusinessManageComponent implements OnInit {
  private  host = Common.URL;
  private userBusinessList: UserBusiness[] = [];
  private searchBusinessManage = new UserBusiness();
  private modifyPermission  = new Permission();

  @ViewChild('adhost', {read: ViewContainerRef})
  private adHost: ViewContainerRef;

  // 当前登录用户
  currentUser: any;

  private listTableComponent: ComponentRef<BusinessTableComponent>[] = [];

  constructor(private http: CustomHttp, private _componentFactoryResolver: ComponentFactoryResolver, private toaster: ToasterService) { }

  ngOnInit() {
    const user = JSON.parse(localStorage.getItem('loginUser'));
    this.currentUser = user.user;
    this.search();
  }

  search() {
    this.clearAllTable();
    let param;
    if (!isUndefined(this.searchBusinessManage.number) && this.searchBusinessManage.number !== '') {
      param = {
        'number': this.searchBusinessManage.number
      };
    }
    this.http.post(this.host + '/merchantUser/search', param)
      .map(res => res.json())
      .subscribe(
        p => {
          this.userBusinessList = [];
          const temps = p.data;
          if (temps) {
            for (let i = 0; i < temps.length; i++) {
              const item = temps[i];
              this.userBusinessList.push(item);
            }
          }
        }
      );
  }

  // 清空所有表格组件。
  private clearAllTable() {
    let comp: ComponentRef<BusinessTableComponent>;
    do {
      comp = this.listTableComponent.pop();
      if (comp) {
        comp.destroy();
      }
    } while (comp);
  }

  /**
   * 强制刷新下级列表。
   */
  forceSearchChild(data) {
    // 检查是否已经展开过。
    for (const item of this.listTableComponent){
      if (item.instance.parentModel.id === data.model.parentId) {
        item.instance.searchChild();
        return;
      }
    }
  }

  searchChild(data) {
    const parentUserBusiness: UserBusiness = data.model;
    // 检查是否已经展开过。
    for (const item of this.listTableComponent){
      if (item.instance.parentModel.id === parentUserBusiness.id) {
        scroll(0, document.body.scrollHeight);
        return;
      }
      if (data.component.path !== item.instance.path && item.instance.path.indexOf(data.component.path) !== -1) {
        this.closeTab(item.instance.path);
      }
    }
    this.http.post(this.host + '/merchantUser/search', {'parentId': parentUserBusiness.id})
      .map(res => res.json())
      .subscribe(
        p => {
          let userBusinessList : UserBusiness[] = [];
          let temps = p['data'];
          if (temps) {
            for (let i = 0; i < temps.length; i++) {
              let item = temps[i];
              userBusinessList.push(item);
            }
            this.createTableComponent(userBusinessList, parentUserBusiness);
          }
        }
      );
  }

  closeTab(path) {
    do {
      let item = this.listTableComponent.pop();
      if (!item) break;
      if (item.instance.path.indexOf(path) >= 0) {
        item.destroy();
      } else {
        this.listTableComponent.push(item);
        break;
      }
    } while (true);
  }

  private createTableComponent(userBusinessList: UserBusiness[], parentUserBusiness: UserBusiness) {
    let componentFactory = this._componentFactoryResolver.resolveComponentFactory(BusinessTableComponent);
    let componentRef = this.adHost.createComponent(componentFactory);
    this.listTableComponent.push(componentRef);

    let newBusinessTable = componentRef.instance;
    newBusinessTable.userBusinessList = userBusinessList;
    newBusinessTable.parentModel = parentUserBusiness;

    let path = '/';

    for (let item of this.listTableComponent){
      path = path + item.instance.parentModel.number + '/';
    }
    newBusinessTable.path = path;
    scroll(0, document.body.scrollHeight);

    newBusinessTable.onExpend.subscribe(data => {
      this.searchChild(data);
    });

    newBusinessTable.onHandle.subscribe(data => {
      this.onTableHandle(data);
    });

    newBusinessTable.onClose.subscribe(data => {
      this.closeTab(data.path);
    });
  }

  private onTableHandle(data){
    if (data.type === 1) {
      this.search();
    }else if (data.type === 2) {
      this.forceSearchChild(data);
    }
  }



  // updatePermissionStatus(id,status){
  //   let per = new Permission();
  //   per.permissionID = id;
  //   per.statusInt = status;
  //   this.updatePermission(per);
  // }


}
