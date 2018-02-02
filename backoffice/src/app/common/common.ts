import {environment} from "../../environments/environment";
export class Common {

  static URL: string = environment.URL;
  //static MESSAGE_URL = 'http://192.168.0.22:9092';
  static MESSAGE_URL = environment.MESSAGE_URL;
  static USER_LANGUAGE = 1;
  static MAX_PAGE_SIZE = 20;
}
