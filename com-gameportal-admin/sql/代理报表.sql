#查询代理信息
SELECT 
u.uiid AS proxyid,
u.account AS proxyaccount,
u.uname AS proxyname,
pu.proxyurl AS proxydomain,
p.clearingtype AS clearingtype,
p.returnscale AS returnscale,
p.ximascale AS ximascale,
p.isximaflag AS ximaStatus,
(SELECT COUNT(lu.uiid) FROM a_user_info lu WHERE lu.puiid = u.uiid) AS lowerUser
FROM a_user_info u LEFT JOIN a_game_proxyinfo p ON u.uiid = p.uiid LEFT JOIN a_proxy_url pu ON u.uiid = pu.proxyuid
WHERE u.accounttype = 1
AND u.uiid=510

#推广数据查询

SELECT * FROM (
SELECT
(SELECT COUNT(lu.uiid) FROM a_user_info lu WHERE lu.puiid = u.uiid AND lu.create_date >= CONCAT('2015-12-08',' ','00:00:00') AND lu.create_date <= CONCAT('2015-12-08',' ','23:59:59')) AS lowecCount,
(SELECT COUNT(DISTINCT ulog.uiid) FROM user_login_log ulog LEFT JOIN a_user_info u2 ON ulog.uiid = u2.uiid WHERE u2.puiid = u.uiid AND ulog.logintime >= CONCAT('2015-12-08',' ','00:00:00') AND ulog.logintime <= CONCAT('2015-12-08',' ','23:59:59')) AS activeUser,
(SELECT COUNT(DISTINCT p.uiid) FROM a_user_info u2 LEFT JOIN a_pay_order p ON u2.uiid = p.uiid WHERE u2.puiid = u.uiid AND p.paytyple=0 AND p.status = 3) AS payusercount,
(SELECT COUNT(p.uiid) FROM a_user_info u2 LEFT JOIN a_pay_order p ON u2.uiid = p.uiid WHERE u2.puiid = u.uiid AND p.paytyple=0 AND p.status = 3) AS paycount,
(SELECT SUM(p.amount) FROM a_user_info u2 LEFT JOIN a_pay_order p ON u2.uiid = p.uiid WHERE u2.puiid = u.uiid AND p.paytyple=0 AND p.status = 3) AS payAmountTotal,
(SELECT COUNT(DISTINCT p.uiid) FROM a_user_info u2 LEFT JOIN a_pay_order p ON u2.uiid = p.uiid WHERE u2.puiid = u.uiid AND p.paytyple=1 AND p.status = 3) AS atmusercount,
(SELECT COUNT(p.uiid) FROM a_user_info u2 LEFT JOIN a_pay_order p ON u2.uiid = p.uiid WHERE u2.puiid = u.uiid AND p.paytyple=1 AND p.status = 3) AS atmcount,
(SELECT SUM(p.amount) FROM a_user_info u2 LEFT JOIN a_pay_order p ON u2.uiid = p.uiid WHERE u2.puiid = u.uiid AND p.paytyple=1 AND p.status = 3) AS atmAmountTotal,
(SELECT SUM(p.amount) FROM a_user_info u2 LEFT JOIN a_pay_order p ON u2.uiid = p.uiid WHERE u2.puiid = u.uiid AND p.paytyple=2 AND p.status = 3 AND p.ordertype IN (1,2,4,5,6,7,8,9,10)) AS preferentialTotal,
(SELECT SUM(p.amount) FROM a_user_info u2 LEFT JOIN a_pay_order p ON u2.uiid = p.uiid WHERE u2.puiid = u.uiid AND p.paytyple=2 AND p.status = 3 AND p.ordertype = 3) AS sdximaAmount,
(SELECT SUM(p.amount) FROM a_user_info u2 LEFT JOIN a_pay_order p ON u2.uiid = p.uiid WHERE u2.puiid = u.uiid AND p.paytyple=3 AND p.status = 3 AND p.ordertype = 0) AS buckleAmount,
(SELECT SUM(px.ximamoney) FROM a_proxyuser_xima_log px WHERE px.status=1 AND px.puiid=510) AS proxyXimaAmount,
(SELECT SUM(cx.total) FROM c_member_xima_main cx LEFT JOIN a_user_info u2 ON cx.uiid = u2.uiid WHERE u2.puiid = u.uiid AND cx.locked=1) AS sysXimaAmount,
(SELECT SUM(ap.realPL) FROM a_proxy_clearing_log ap WHERE ap.uiid=u.uiid AND ap.clearingStatus = 2) AS recordAmount
FROM
a_user_info u
WHERE u.accounttype = 1 
AND u.uiid = 510
) AS tmp

`betdate`
#统计游戏数据
SELECT 
COUNT(a.pdid) AS betTotel,
SUM(a.betamount) AS betAmountTotal,
SUM(a.validBetAmount) AS validBetAmountTotal,
SUM(a.finalamount) AS winlossTotal
FROM a_user_info u LEFT JOIN a_game_betlog a ON u.account = a.account
WHERE u.puiid = 510


