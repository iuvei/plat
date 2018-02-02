import {Pipe, PipeTransform} from "@angular/core";
@Pipe({ name: 'AccountStatusPipe' })
export class AccountStatusPipe implements PipeTransform {

  transform(value: any) {
    let str = '';
    if (value === 3) {
      str = 'common.locked';
    } else if (value === 2) {
      str = 'common.disable';
    } else if (value === 1) {
      str = 'common.normal';
    }
    return str;
  }
}
