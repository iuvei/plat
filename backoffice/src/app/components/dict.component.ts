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
  selector: 'dict-control',
  templateUrl: 'dict.component.html',
  styleUrls: ['dict.component.scss'],
  encapsulation: ViewEncapsulation.None,
})
export class DictComponent implements  OnInit{
  @Input() id: string = null;
  @Input("dict-type") dicttype: string = null;
  @Input() name: string = null;
  @Input("d-class") clazz: string = null;
  @Input("default-info") defaultInfo: string = null;
  @Output() valChange = new EventEmitter();
  _val: string = null;

  private dicts:Dict[];

  ngOnInit(): void {
    this.dicts = [];
    if(this.defaultInfo){
      let dict = new Dict();
      dict.name = this.defaultInfo;
      dict.value='';
      this.dicts[0]=dict;
    }
    let temp = JSON.parse(localStorage['loginUser']).dicts[this.dicttype] as Dict[];
    for(let item of temp){
      this.dicts.push(item);
    }
  }


  get val() {
    return this._val;
  }

  @Input()
  set val(value) {
    this._val = value;
    this.valChange.emit(value);
  }
}
