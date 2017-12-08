
$(function () {

    getJackpot();

    getWiner();

    getPos('#j-superman',350);

   showModal();


    function showModal() {
        var $asideAd=$('#aside-ad');
        setTimeout(function () {
            $('#modal-index').modal('show');
            $asideAd.addClass('active');
        }, 1000);

        $asideAd.find('.close').on('click', function () {
            $asideAd.removeClass('active');
        });
    }

    //奖池数据
    function getJackpot() {
        var options = {
            useEasing: true,
            useGrouping: true,
            separator: ',',
            decimal: '.',
            prefix: '',
            suffix: ''
        };
        var demo = new CountUp("j-jackpot", 39824338, 99824338, 2, 3000000000, options);
        demo.start();
    }

    //生成中奖喜讯
    function getWiner() {

            var $box = $("#j-marquee-aside"),
                $items = $box.find("ul");
            var length = $items.children().length;
            //动画，小于5条时无动画
            if (length < 6) {
                return false;
            }
            var ci = setInterval(run, 3000);
            $items.hover(function () {
                clearInterval(ci);
            }, function () {
                ci = setInterval(run, 3000);
            });

            function run() {
                $items.animate({
                    "margin-top": "-65px"
                }, 500, function () {
                    $items.find("li:eq(0)").appendTo($items.css({
                        "margin-top": "0"
                    }));
                });
            }

        }

    function getPos(target,num){
        var $window=$(window),
            $this=(typeof target === 'string') ? $(target) : target,
            top=num;
        function checkPosition(){
            $window.scrollTop() > top ? $this.addClass("active") : $this.removeClass("active")
        }

        checkPosition();

        $window.on("scroll", checkPosition);
        $window.on("resize", checkPosition);
    };
});

