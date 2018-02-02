import { Injectable } from '@angular/core';
import { Http, Request, RequestOptionsArgs, Response, RequestOptions, ConnectionBackend, Headers } from '@angular/http';
import 'rxjs/Rx';
import {Observable} from 'rxjs/Observable';
import {PubSubService} from "./pubsup.service";

import {LoadingAnimateService} from "ng2-loading-animate";
import {TranslateService} from "@ngx-translate/core";
import {TimeoutError} from "rxjs/util/TimeoutError";
import {Router} from "@angular/router";
import {ConfirmationService} from "primeng/components/common/api";
import {isUndefined} from "util";
@Injectable()
export class CustomHttp extends Http {
  _pubsub: PubSubService;
  _router :Router;

  constructor(backend: ConnectionBackend, defaultOptions: RequestOptions, pubsub: PubSubService,
              private _loadingSvc: LoadingAnimateService, private _translate: TranslateService, router: Router, private confirmDialog:ConfirmationService) {
    super(backend, defaultOptions);
    this._pubsub = pubsub;
    this._router = router;
  }
  request(url: string | Request, options ?: RequestOptionsArgs): Observable < Response > {
    this._loadingSvc.setValue(true);
    return this.intercept(super.request(url, this.getRequestOptionArgs(options)).timeout(40000))
      .catch(this.handleError(this));
  }
  get(url: string, options ?: RequestOptionsArgs): Observable < Response > {
    return this.intercept(super.get(url, this.getRequestOptionArgs(options)));
  }
  post(url: string, body: any, options ?: RequestOptionsArgs): Observable < Response > {
    return this.intercept(super.post(url, body, this.getRequestOptionArgs(options)));
  }
  put(url: string, body: string, options ?: RequestOptionsArgs): Observable < Response > {
    return this.intercept(super.put(url, body, this.getRequestOptionArgs(options)));
  }
  delete(url: string, options ?: RequestOptionsArgs): Observable < Response > {
    return this.intercept(super.put(url, this.getRequestOptionArgs(options)));
  }
  getRequestOptionArgs(options ?: RequestOptionsArgs): RequestOptionsArgs {
    if (options == null) {
      options = new RequestOptions();
    }
    if (options.headers == null) {
      options.headers = new Headers();
      options.headers.append('Content-Type', 'application/json');
    }

    const currentUser = JSON.parse(localStorage.getItem('loginUser'));
    if (currentUser && currentUser.user && currentUser.user.token) {
      options.headers.append('Authorization', currentUser.user.token);
    }

    return options;
  }
  intercept(observable: Observable < Response > ): Observable < Response > {
    return Observable.create((observer) => {
      observable.subscribe(res => {
        const ok = res.headers.get('ok');
        const statusText = res.headers.get('statusText');
        if (res.ok) {
          try{
            res.json();
            console.info(res);
            if(!isUndefined(res.json().msg) && (ok ==null || statusText==null)) {
              if(decodeURI(res.json().msg).indexOf("TOKEN") !=-1) {
                this.confirmDialog.confirm({
                  message: decodeURI(res.json().msg),
                  accept: () =>{
                    this._router.navigate(['/login']);
                  }
                });
              }else{
                if(res.type ==2){
                  this._pubsub.errorToast.emit(res.json().msg);
                }else{
                  observer.next(res);
                }
              }
            }else if ((ok && ok === 'false')) {
              if(decodeURI(statusText).indexOf("TOKEN") !=-1) {
                this.confirmDialog.confirm({
                  message: decodeURI(statusText),
                  accept: () =>{
                    this._router.navigate(['/login']);
                  }
                });
              }else{
                this._pubsub.errorToast.emit(decodeURI(statusText));
              }
            }else {
              observer.next(res);
            }
          } catch (err) {
            observer.error(err);
          }
        }
      }, (err) => {
        observer.error(err);
      }, () => {
        this._loadingSvc.setValue(false);
        // 注意添加这句，否则有可能一些第三方的包不能正常使用，如ng2-translate
        observer.complete();
      });
    })
  }
  handleError(err) {
    return (res) =>{
      console.log(res);
      this._loadingSvc.setValue(false);
      if (res instanceof Response) {
        this._pubsub.errorToast.emit(this._translate.instant('common.systemError'));
        // if (res.status === 401 || res.status === 403) {
        //   console.log(res);
        // }
      } else if (res instanceof TimeoutError) {
        this._pubsub.errorToast.emit(this._translate.instant('common.timeOutError'));
      } else {
        this._pubsub.errorToast.emit(this._translate.instant('common.transfJsonError'));
      }
      return Observable.throw(res);
    };
  }

}
