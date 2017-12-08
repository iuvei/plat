$(function () {
    //日历控件
    var $body = $("body"); //定义控件响应的容器
    $body.click(function () { //点击容器关闭控件
        $(this).find(".ui-calendar").hide();
    }).find(".ui-datePicker").click(function (e) { //阻止冒泡并显示控件
        e.stopPropagation();
        $(this).find(".ui-calendar").show();
        $(this).siblings(".ui-datePicker").find(".ui-calendar").hide();
    }).find(".ui-calendar").each(function () { //初始化控件
        $(this).calendar();
    }).delegate(".dates li:not('.gray')", 'click', function (e) {
        e.stopPropagation();
        $(this).closest(".ui-calendar").hide().next("input").val($(this).data("date")).change();
    });

    //下拉控件
    $.ddl();

    //分页控件
    /*$("#pages").paging({
        currentPage: 1, //当前页
        totalRecoreds: 100, //总计录数
        recordsPerPage: 10//每页X条
    });*/
});