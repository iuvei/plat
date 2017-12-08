$(function () {
    Date.prototype.Format = function (fmt) {
        var o = {
            "M+": this.getMonth() + 1,
            "d+": this.getDate(),
            "h+": this.getHours(),
            "m+": this.getMinutes(),
            "s+": this.getSeconds(),
            "q+": Math.floor((this.getMonth() + 3) / 3),
            "S": this.getMilliseconds()
        };
        if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
        for (var k in o)
            if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
        return fmt;
    };
    var html = document.documentElement;
    var uaInfo = {
        ua: '',
        is: function (t) {
            return RegExp(t, "i").test(uaInfo.ua);
        },
        version: function (p, n) {
            n = n.replace(".", "_");
            var i = n.indexOf('_'),
                ver = "";
            while (i > 0) {
                ver += " " + p + n.substring(0, i);
                i = n.indexOf('_', i + 1);
            }
            ver += " " + p + n;
            return ver;
        },
        getBrowser: function () {
            var g = 'gecko',
                w = 'webkit',
                c = 'chrome',
                f = 'firefox',
                s = 'safari',
                o = 'opera',
                a = 'android',
                bb = 'blackberry',
                dv = 'device_',

                ua = uaInfo.ua,
                is = uaInfo.is;

            return [
                (!(/opera|webtv/i.test(ua)) && /msie\s(\d+)/.test(ua)) ? ('ie ie' + (/trident\/4\.0/.test(ua) ? '8' : RegExp.$1))
                    : is('firefox/') ? g + " " + f + (/firefox\/((\d+)(\.(\d+))(\.\d+)*)/.test(ua) ? ' ' + f + RegExp.$2 + ' ' + f + RegExp.$2 + "_" + RegExp.$4 : '')
                    : is('gecko/') ? g
                    : is('opera') ? o + (/version\/((\d+)(\.(\d+))(\.\d+)*)/.test(ua) ? ' ' + o + RegExp.$2 + ' ' + o + RegExp.$2 + "_" + RegExp.$4 : (/opera(\s|\/)(\d+)\.(\d+)/.test(ua) ? ' ' + o + RegExp.$2 + " " + o + RegExp.$2 + "_" + RegExp.$3 : ''))
                    : is('konqueror') ? 'konqueror'
                    : is('blackberry') ? (bb + (/Version\/(\d+)(\.(\d+)+)/i.test(ua) ? " " + bb + RegExp.$1 + " " + bb + RegExp.$1 + RegExp.$2.replace('.', '_') : (/Blackberry ?(([0-9]+)([a-z]?))[\/|;]/gi.test(ua) ? ' ' + bb + RegExp.$2 + (RegExp.$3 ? ' ' + bb + RegExp.$2 + RegExp.$3 : '') : ''))) // blackberry
                    : is('android') ? (a + (/Version\/(\d+)(\.(\d+))+/i.test(ua) ? " " + a + RegExp.$1 + " " + a + RegExp.$1 + RegExp.$2.replace('.', '_') : '') + (/Android (.+); (.+) Build/i.test(ua) ? ' ' + dv + ((RegExp.$2).replace(/ /g, "_")).replace(/-/g, "_") : '')) //android
                    : is('chrome') ? w + ' ' + c + (/chrome\/((\d+)(\.(\d+))(\.\d+)*)/.test(ua) ? ' ' + c + RegExp.$2 + ((RegExp.$4 > 0) ? ' ' + c + RegExp.$2 + "_" + RegExp.$4 : '') : '')
                    : is('iron') ? w + ' iron'
                    : is('applewebkit/') ? (w + ' ' + s + (/version\/((\d+)(\.(\d+))(\.\d+)*)/.test(ua) ? ' ' + s + RegExp.$2 + " " + s + RegExp.$2 + RegExp.$3.replace('.', '_') : (/ Safari\/(\d+)/i.test(ua) ? ((RegExp.$1 == "419" || RegExp.$1 == "417" || RegExp.$1 == "416" || RegExp.$1 == "412") ? ' ' + s + '2_0' : RegExp.$1 == "312" ? ' ' + s + '1_3' : RegExp.$1 == "125" ? ' ' + s + '1_2' : RegExp.$1 == "85" ? ' ' + s + '1_0' : '') : ''))) //applewebkit
                    : is('mozilla/') ? g : ''
            ];
        },
        getPlatform: function () {
            var ua = uaInfo.ua,
                version = uaInfo.version,
                is = uaInfo.is;

            return [
                is('j2me') ? 'j2me'
                : is('ipad|ipod|iphone') ? (
                    (/CPU( iPhone)? OS (\d+[_|\.]\d+([_|\.]\d+)*)/i.test(ua) ? 'ios' + version('ios', RegExp.$2) : '') + ' ' + (/(ip(ad|od|hone))/gi.test(ua) ? RegExp.$1 : "")) //'iphone'
                //:is('ipod')?'ipod'
                //:is('ipad')?'ipad'
                : is('playbook') ? 'playbook'
                : is('kindle|silk') ? 'kindle'
                : is('playbook') ? 'playbook'
                : is('mac') ? 'mac' + (/mac os x ((\d+)[.|_](\d+))/.test(ua) ? (' mac' + (RegExp.$2) + ' mac' + (RegExp.$1).replace('.', "_")) : '')
                : is('win') ? 'win' + (is('windows nt 6.2') ? ' win8' : is('windows nt 6.1') ? ' win7' : is('windows nt 6.0') ? ' vista' : is('windows nt 5.2') || is('windows nt 5.1') ? ' win_xp' : is('windows nt 5.0') ? ' win_2k' : is('windows nt 4.0') || is('WinNT4.0') ? ' win_nt' : '')
                : is('freebsd') ? 'freebsd'
                : is('x11|linux') ? 'linux' : ''
            ];
        },
        getMobile: function () {
            var is = uaInfo.is;
            return [
                is("android|mobi|mobile|j2me|iphone|ipod|ipad|blackberry|playbook|kindle|silk") ? 'mobile' : ''
            ];
        }
    };

    function init(u) {
        /* ua */
        uaInfo.ua = u.toLowerCase();
    }

    // init
    init(navigator.userAgent);

    document.getElementById('browser').innerHTML = uaInfo.getBrowser();
    document.getElementById('os').innerHTML = uaInfo.getPlatform();
    document.getElementById('lang').innerHTML = navigator.language ? navigator.language : navigator.userLanguage || "";

    //document.getElementById('serverT').innerHTML = serverTime;
    document.getElementById('localT').innerHTML = new Date().Format("yyyy年MM月dd日 hh:mm:ss");
    var gmt = new Date().getTimezoneOffset() / -60;
    gmt = gmt >= 0 ? "+" + gmt : gmt;
    document.getElementById('GMT').innerHTML = '本地' + gmt;

    //document.getElementById('cdn').innerHTML = cdn ? 'ok' : 'no';
    //document.getElementById('ip').innerHTML = ip;

    var mobile = uaInfo.getMobile();
    document.getElementById('mobile').innerHTML = !mobile[0] ? '否' : mobile[0];
    document.getElementById('screen').innerHTML = '宽度：' + (window.outerWidth || html.clientWidth) + 'px &nbsp;&nbsp;' + '高度' + (window.outerHeight || html.clientHeight) + 'px';
    document.getElementById('renderTime').innerHTML = (new Date().getTime() - beginTime) + 'ms';
});