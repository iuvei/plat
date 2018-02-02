import {
  Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges,
  ViewEncapsulation
} from "@angular/core";
import {Dict} from "../model/dict";
import {ControlValueAccessor} from "@angular/forms";
/**
 * Created by Administrator on 2017/6/24 0024.
 */
@Component({
  selector: 'money',
  templateUrl:'money.component.html',
  encapsulation: ViewEncapsulation.None,
})
export class MoneyComponent implements  OnInit{
  @Input("value") value: number = null;
  ngOnInit(): void {
  }

  private moneyColor(money){
    if(money>0){
      return "green";
    }else if(money==0){
      return "";
    }else{
      return "red";
    }
  }

}
