$(function() {
	var $items = $("#items").find("li"), $btnBonus = $("#btn-bonus"), len = $items.length;

	// 随机灯
	var randomLights = function() {
		var $lights = $("#lights").find("li"), indexLights = 0

		setInterval(function() {
			$lights.filter(".active").removeClass("active");
			for (var i = 0; i < 10; i++) {
				indexLights = (indexLights + Math.floor(Math.random() * len))
						% len;
				$lights.eq(indexLights).addClass("active");
			}
		}, 300);
	}
	randomLights();

	// 抽奖效果
	var Bonus = function() {
		// 初始化变量
		var arrBonus = [ [ 100 ], [ 2, 5, 7, 12, 15, 17 ],
				[ 0, 4, 10, 14, 16 ], [ 9, 19 ], [ 6 ], [ 3, 13 ], [ 8, 18 ],
				[ 1 ], [ 11 ] ], arrSpeedUp = [ 1000, 500, 250, 125, 64, 32, 20 ], arrSlowDown = [
				20, 32, 64, 125, 250, 500, 1000 ], i, l, interval, count = 0, // 奖品的索引位置
		objBonus = {
			bonusId : 0
		}, stopPoint, // 减速点
		fStopPoint; // 减速阶段是否已到达减速点

		// 开始
		this.start = function() {
			i = 0, // count = 0,
			l = arrSpeedUp.length;
			objBonus.error = false;

			setBonus();
			runSpeedUp(arrSpeedUp[0]);
		}

		// 停止
		this.stop = function() {
			if (objBonus.error) {
				return false;
			}

			// 设置减速点
			var _len = arrBonus[objBonus.bonusId].length, _index = Math
					.floor(Math.random() * _len), index = arrBonus[objBonus.bonusId][_index] - 6;
			stopPoint = (index + len) % len, fStopPoint = false;

			// 初始化减速
			i = 0, l = arrSlowDown.length;
			clearInterval(interval);
			runSlowDown(arrSlowDown[0]);
		}

		// 传入获奖信息
		this.setObjBonus = function(obj) {
			objBonus = obj;

			if (objBonus.error) {
				$.remind("服务器超时，请重试！", function() {
					window.location.reload();
				});
			}
		}

		// 获取奖品
		function getBonus() {
			count = (++count) % len;
			setBonus();
		}

		// 设置奖品
		function setBonus() {
			$items.filter(".active").removeClass("active");
			$("#set-bonus").html($items.eq(count).addClass("active").html());
		}

		// 加速
		function runSpeedUp(time) {
			setTimeout(function() {
				i++;
				if (i < l) {
					getBonus();
					runSpeedUp(arrSpeedUp[i]);
				} else {
					interval = setInterval(function() {
						getBonus();
					}, 20);
				}
			}, time);
		}

		// 减速,需到达减速点
		function runSlowDown(time) {
			setTimeout(function() {
				if (stopPoint === count)
					fStopPoint = true;
				if (fStopPoint)
					i++;
				if (i < l) {
					getBonus();
					runSlowDown(arrSlowDown[i]);
				} else {
					$("#left").text(function() {
						var left = $(this).text() * 1 - 1
						if (left > 0) {
							$btnBonus.removeClass("disabled");
						}
						return left;
					});
					$("#used").text(function() {
						return $(this).text() * 1 + 1
					});
					addMyCoupon();
				}
			}, time);
		}

		// 加入我的优惠券
		function addMyCoupon() {
			var map = [ 0, 1, 2, 3, 4, 5, 6, 7, 8 ], $source = $items
					.filter(".active"), $clone = $source.clone().addClass(
					"clone").insertAfter($source), $target = $("#my-coupon")
					.find("li").eq(map[objBonus.bonusId]), tOffSet = $target
					.position();

			$clone.css({
				margin : "660px 0 0 " + (184 + tOffSet.left) + "px",
			}).addClass("run");
			setTimeout(function() {
				updateMyCoupon(map[objBonus.bonusId]);
				$clone.fadeOut("fast", function() {
					$clone.remove();
				});
			}, 1000);
		}

	} // Bonus

	var bonus = new Bonus();
	$btnBonus.click(function() {
		var canLottery = $("#canLottery").val();

		if (canLottery == 1) {
			var leftNum = parseInt($("#left").text());
			var usedNum = parseInt($("#used").text());
			if(leftNum <=0 ){
				if(usedNum == 10){
					$.remind("您今日的抽奖机会已用完，请明天再来！", function() {}, "确认");
				}else{
					$.remind("您暂无抽奖机会，去存款活动获取抽奖机会！", function() {}, "确认");
				}
				return false;
			}
			if ($(this).hasClass("disabled"))
				return false;
			$(this).addClass("disabled");
			bonus.start();

			$.ajax({
				type : 'get',
				url : '/lottery/submit.html',
				dataType : 'json',
				success : function(data) {
					var result = data.result;
					if (result.flag == '1001') {
						$.remind("您还未登录，请登录后再来抽奖！", function() {
							location.href = "/login.html";
						}, "确认");
					} else if (result.flag == 1) {
						// 模拟抽奖
						bonus.setObjBonus({
							bonusId : result.tempId
						});
					} else {
						bonus.stop();
						$.remind(result.msg, function() {
						});
					}
				}
			});

			// 设置超时
			var timeout = setTimeout(function() {
				bonus.setObjBonus({
					error : true
				});
			}, 10000);

			setTimeout(function() {
				clearTimeout(timeout);
				bonus.stop();
			}, 5000);
		} else {
			$.remind("您还未登录，请登录后再来抽奖！", function() {
				location.href = "/login.html";
			}, "确认");
		}
	});

	// 生成中奖信息
	function bonusNews() {
		// var json = [{
		// "text": "ab***01 获得 38元现金",
		// "time": "26/04 10:34"
		// }, {
		// "text": "ab***02 获得 38元现金",
		// "time": "26/04 10:34"
		// }, {
		// "text": "ab***03 获得 38元现金",
		// "time": "26/04 10:34"
		// }, {
		// "text": "ab***04 获得 38元现金",
		// "time": "26/04 10:34"
		// }, {
		// "text": "ab***05 获得 38元现金",
		// "time": "26/04 10:34"
		// }, {
		// "text": "ab***06 获得 38元现金",
		// "time": "26/04 10:34"
		// }, {
		// "text": "ab***07 获得 38元现金",
		// "time": "26/04 10:34"
		// }, {
		// "text": "ab***08 获得 38元现金",
		// "time": "26/04 10:34"
		// }, {
		// "text": "ab***09 获得 38元现金",
		// "time": "26/04 10:34"
		// }, {
		// "text": "ab***10 获得 38元现金",
		// "time": "26/04 10:34"
		// }, {
		// "text": "ab***11 获得 38元现金",
		// "time": "26/04 10:34"
		// }],
		// length = json.length;

		// 插入文本
		var $e = $(), $box = $("#bonus-news"), $items = $box.find("ul");
		length = $box.find("li").length;
		// for (var i = 0; i < length; i++) {
		// $e = $e.add($("<li />").html("<span class='text'>" + json[i].text +
		// "</span><span class='time'>" + json[i].time + "</span>"));
		// }
		// $items.append($e);

		// 动画，小于5条时无动画
		if (length < 9) {
			return false;
		}
		var ci = setInterval(run, 3000);
		$items.hover(function() {
			clearInterval(ci);
		}, function() {
			ci = setInterval(run, 3000);
		});

		function run() {
			$items.animate({
				"margin-top" : "-59px"
			}, 500, function() {
				$items.find("li:eq(0)").appendTo($items.css({
					"margin-top" : "0"
				}));
			});
		}

	} // bonusNews
	bonusNews();

	// 更新我的优惠券
	function updateMyCoupon(index) {
		// var arr = [90, 91, 92, 93, 94, 95, 96, 97];
		// $("#my-coupon").find("li:gt(0)").find("span").map(function (index) {
		// $(this).text(arr[index]);
		// });
		$obtainPrize = $("#my-coupon").find("li").eq(index).find("span");
		$obtainPrize.text(parseInt($obtainPrize.text()) + 1);

	}
	// updateMyCoupon();
});