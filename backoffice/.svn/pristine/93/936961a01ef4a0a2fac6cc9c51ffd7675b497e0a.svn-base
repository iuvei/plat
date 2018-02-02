import {Pipe, PipeTransform} from "@angular/core";
@Pipe({ name: 'AccountBetStatusPipe' })
export class AccountBetStatusPipe implements PipeTransform {

  transform(value: any) {
    let str = '';
    if (value === 2) {
      str = 'common.disable';
    } else if (value === 1) {
      str = 'common.normal';
    }
    return str;
  }
}
