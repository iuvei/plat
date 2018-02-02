/**
 * Created by sunny on 2017/4/5 0005.
 */
export class SearchAccountRecord{
  public startTime;
  public endTime;
  public userName;
  public currentPage ;
  public pageSize = 50;

  public static create(searchAccountRecord){
    let search = new SearchAccountRecord();
    search.currentPage = searchAccountRecord.currentPage;
    search.pageSize = searchAccountRecord.pageSize;
    search.userName = searchAccountRecord.userName;
    search.startTime = new Date(searchAccountRecord.startTime);
    search.endTime = new Date(searchAccountRecord.endTime);
    return search;
  }
}
