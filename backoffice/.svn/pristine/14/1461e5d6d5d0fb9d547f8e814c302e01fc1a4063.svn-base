import {Pipe, PipeTransform} from "@angular/core";
import {Dict} from "../model/dict";
/**
 * Created by sunny on 2017/4/5 0005.
 */
@Pipe({ name: 'DictPipe' })
export class DictPipe implements PipeTransform {
  private dicts;
  constructor() {
    console.info("dictPipe...");
    this.dicts = JSON.parse(localStorage['loginUser']).dicts;
  }

  transform(value: any, param) {
    let list : any[];
    if(param instanceof Array){
      list = param;
    }else {
      let dictType:string = param;
      list = this.dicts[dictType];
    }

    for (let i=0;i<list.length;i++){
      let item = list[i];
      if (
        (item.asValue && item.asValue==value)
        || (item.value==value)
      ){
        return list[i].name;
      }
    }
    return value;
  }
}
