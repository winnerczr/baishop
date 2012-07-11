$.fn.extend({
	trunAd3: function(options) {
		var auto = null;
		var obj = $(this);
		ad3count = $("a", obj).size();
		ad3n = 0;
		var settings = { timer: 3600000, menu: "#shoesPlayText" };
		options = options || {};
		$.extend(settings, options);
		var ulcontent = "<ul>";
		for (i = 1; i <= ad3count; i++) { ulcontent = ulcontent + "<li>" + i + "</li>"; }
		ulcontent = ulcontent + "</ul>";
		$(settings.menu).html(ulcontent);
		$("a:not(:first-child)", this).hide();
		$(settings.menu + " li").eq(0).css({ "background": "#7f0019", "color": "#fff", "font-weight": "bolder" });
		$(settings.menu + " li").mouseover(function() {
			i = $(this).text() - 1;
			ad3n = i;
			if (ad3n >= ad3count) return;
			$("a", obj).filter(":visible").fadeOut(200, function() { $(this).parent().children().eq(ad3n).fadeIn(300); });
			$(this).css({ "background": "#7f0019", "color": "#fff", "font-weight": "bolder" }).siblings().css({ "background": "#fff", "color": "#7f0019", "font-weight": "normal" });
		});
		auto = setInterval(showAuto, settings.timer);
		obj.hover(function() { clearInterval(auto) }, function() { auto = setInterval(showAuto, settings.timer); });
		function showAuto() {
			ad3n = ad3n >= (ad3count - 1) ? 0 : ++ad3n;
			$(settings.menu + " li").eq(ad3n).trigger('mouseover');
		}
	}
});