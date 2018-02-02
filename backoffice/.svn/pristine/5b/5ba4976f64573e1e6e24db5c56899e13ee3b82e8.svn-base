import {Component} from "@angular/core";
import {TranslateService} from "@ngx-translate/core";
import {Http} from "@angular/http";
import {ActivatedRoute, Router} from "@angular/router";
import "rxjs/add/operator/map";
import {MyRequestOptions} from "../components/MyRequestOptions";
import {Common} from "../common/common";

@Component({
  templateUrl: 'login.component.html'
})
export class LoginComponent {
  public userName : string;
  public password : string;
  public verifCode : string;
  public message : string;
  public token : string;
  public verifCodeURL : string ;
  public languages = {
    "zh-CN":1,
    "zh-TW":2,
    "en-US":3,
    "sk-SU":4
  };
  constructor(private translate: TranslateService, private http:Http, private route:Router,private opt : MyRequestOptions,private routeA: ActivatedRoute){
  }

  ngOnInit() {
    let token = this.routeA.snapshot.queryParams.token;
    if(token!=null){
      this.token = token;
      this.login();
    }
  }

  login(){
    this.http.post(Common.URL+"/login",{
      'username':this.userName,
      'password':this.password,
      'language':Common.USER_LANGUAGE,
      'token' : this.token,
      'captcha':this.verifCode},
      { withCredentials: true})
      .map(res =>res.json())
      .subscribe(item => {
        if (item.status == 0) {
          sessionStorage.clear();
          localStorage['loginUser'] = JSON.stringify(item.data);
          let currentUser = JSON.parse(localStorage['loginUser']);
          //区分首页
          let roles = currentUser.user.roles as any[];
          for (let role of roles) {
            if (role.roleName == 'API' || role.roleName == 'ApiAgent') {
              this.route.navigate(['/api']);
              return;
            } else if (role.roleName == 'Agent') {
              this.route.navigate(['/agent']);
              return;
            }
          }
          this.route.navigate(['/sys']);
        } else {
          this.changeVerifCodeURL();
          this.message = item.msg;
        }
      });
  }

  changeVerifCodeURL(){
    if(this.userName == null || this.userName == '')
        return;
    this.verifCodeURL = Common.URL+'/codeimg?username='+this.userName+"&"+Math.random();
  }

  changeLanguage(language:string){
    this.translate.use(language);
    Common.USER_LANGUAGE = this.languages[language];
  }
}
