import {Pipe, PipeTransform} from "@angular/core";
@Pipe({ name: 'AccountIsActiveTitlePipe' })
export class AccountIsActiveTitlePipe implements PipeTransform {

  transform(value: any) {
    let str = '';
    if (typeof (value) === 'undefined' || value === '') {
      str = 'account.active';
    } else {
      str = 'account.inactive';
    }

    return str;
  }
}
