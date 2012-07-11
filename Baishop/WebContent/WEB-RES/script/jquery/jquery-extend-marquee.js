/********************************************************************************************************
 * 列表滚动插件
 *----------------------------------------------------------------------------------------------------
 * @Desc 列表滚动插件
 *----------------------------------------------------------------------------------------------------
 * @Author 鲍志飞
 * @Email baozhifei@gmail.com
 * @QQ 356432953
 * @Blog http://blog.csdn.net/baozhifei
 * @Date 2011-05-03
 * @Version V1.0@2011-05-03
 * @JQueryVersion 1.3.2+ 
 * 
 * @update  
 **/
(function($) {
	$.fn.marquee = function(speed, delay) {
		
		// ul是我们实际要滚动的对象，改变其scrollTop属性，使其向上滚动
		var _ul = $(this);
		
		// ul中li的个数，当我们滚动一个来回的时候，我们要重置ul的scrollTop为0，使其重新滚动
		// 所以知道li的个数，才能判断什么时候滚动一个来回了
		var _liSize = _ul.find('li').size();
		
		// li的高度，每滚动一个li这样高度，我们要停顿一下，然后再滚动
		// 所以知道li的高度，才能判断什么时候该停顿了
		var _liHeight = _ul.find('li:first').height();
		
		// 当用户的鼠标停在ul上方时，我们要暂停滚动
		var _pause = false;
		
		// 定时句柄
		var _intervalId;
		
		// 第一次滚动，scrollTop要置0
		_ul.scrollTop(0);
		
		// 为了使后面的元素滚动到顶部时，不会出现断档空白的现象，我们复制所有的li，再追加到ul中去
		// 这样即使是最后一个li滚动到顶部时，下面也不会出现空白
		_ul.append(_ul.html());		
		
		// 处理鼠标悬停事件
		$(this).hover(function() {
			_pause = true;
		}, function() {
			_pause = false;
		});
		
		// 开始滚动函数
		var start = function() {
			_intervalId = setInterval(scrolling, speed);
			if (!_pause) {
				_ul.scrollTop(_ul.scrollTop() + 1);
			}
		}
		
		// 滚动函数
		var scrolling = function() {
			if ((_ul.scrollTop() % _liHeight) != 0) {
				// 还没滚动到一个li的高度，我们继续滚动
				_ul.scrollTop(_ul.scrollTop() + 1);
			} else {
				// 正好滚动一个li的高度时
				if ((_ul.scrollTop() / _liHeight) == _liSize) {
					// 正好滚动一个来回时，我们重置ul的scrollTop为0
					_ul.scrollTop(0);
				}
				
				// 正好滚动一个li的高度时，我们停顿一下
				clearInterval(_intervalId);
				setTimeout(start, delay);
			}
		}
		
		setTimeout(start, delay);
	}
})(jQuery);