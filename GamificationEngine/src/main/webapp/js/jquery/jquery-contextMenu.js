if(jQuery){(function(){$.extend($.fn,{contextMenu:function(o,_1){if(o.menu==undefined){return false;}if(o.inSpeed==undefined){o.inSpeed=150;}if(o.outSpeed==undefined){o.outSpeed=75;}if(o.inSpeed==0){o.inSpeed=-1;}if(o.outSpeed==0){o.outSpeed=-1;}$(this).each(function(){var el=$(this);var _2=$(el).offset();$("#"+o.menu).addClass("contextMenu");$(this).mousedown(function(e){var _3=e;$(this).mouseup(function(e){var _4=$(this);$(this).unbind("mouseup");if(_3.button==2){$(".contextMenu").hide();var _5=$("#"+o.menu);if($(el).hasClass("disabled")){return false;}var d={},x,y;if(self.innerHeight){d.pageYOffset=self.pageYOffset;d.pageXOffset=self.pageXOffset;d.innerHeight=self.innerHeight;d.innerWidth=self.innerWidth;}else{if(document.documentElement&&document.documentElement.clientHeight){d.pageYOffset=document.documentElement.scrollTop;d.pageXOffset=document.documentElement.scrollLeft;d.innerHeight=document.documentElement.clientHeight;d.innerWidth=document.documentElement.clientWidth;}else{if(document.body){d.pageYOffset=document.body.scrollTop;d.pageXOffset=document.body.scrollLeft;d.innerHeight=document.body.clientHeight;d.innerWidth=document.body.clientWidth;}}}(e.pageX)?x=e.pageX:x=e.clientX+d.scrollLeft;(e.pageY)?y=e.pageY:x=e.clientY+d.scrollTop;$(document).unbind("click");$(_5).css({top:y,left:x}).fadeIn(o.inSpeed);$(_5).find("A").mouseover(function(){$(_5).find("LI.hover").removeClass("hover");$(this).parent().addClass("hover");}).mouseout(function(){$(_5).find("LI.hover").removeClass("hover");});$(document).keypress(function(e){switch(e.keyCode){case 38:if($(_5).find("LI.hover").size()==0){$(_5).find("LI:last").addClass("hover");}else{$(_5).find("LI.hover").removeClass("hover").prevAll("LI:not(.disabled)").eq(0).addClass("hover");if($(_5).find("LI.hover").size()==0){$(_5).find("LI:last").addClass("hover");}}break;case 40:if($(_5).find("LI.hover").size()==0){$(_5).find("LI:first").addClass("hover");}else{$(_5).find("LI.hover").removeClass("hover").nextAll("LI:not(.disabled)").eq(0).addClass("hover");if($(_5).find("LI.hover").size()==0){$(_5).find("LI:first").addClass("hover");}}break;case 13:$(_5).find("LI.hover A").trigger("click");break;case 27:$(document).trigger("click");break;}});$("#"+o.menu).find("A").unbind("click");$("#"+o.menu).find("LI:not(.disabled) A").click(function(){$(document).unbind("click").unbind("keypress");$(".contextMenu").hide();if(_1){_1($(this).attr("href").substr(1),$(_4),{x:x-_2.left,y:y-_2.top,docX:x,docY:y});}return false;});setTimeout(function(){$(document).click(function(){$(document).unbind("click").unbind("keypress");$(_5).fadeOut(o.outSpeed);return false;});},0);}});});if($.browser.mozilla){$("#"+o.menu).each(function(){$(this).css({"MozUserSelect":"none"});});}else{if($.browser.msie){$("#"+o.menu).each(function(){$(this).bind("selectstart.disableTextSelect",function(){return false;});});}else{$("#"+o.menu).each(function(){$(this).bind("mousedown.disableTextSelect",function(){return false;});});}}$(el).add("UL.contextMenu").bind("contextmenu",function(){return false;});});return $(this);},disableContextMenuItems:function(o){if(o==undefined){$(this).find("LI").addClass("disabled");return ($(this));}$(this).each(function(){if(o!=undefined){var d=o.split(",");for(var i=0;i<d.length;i++){$(this).find("A[href=\""+d[i]+"\"]").parent().addClass("disabled");}}});return ($(this));},enableContextMenuItems:function(o){if(o==undefined){$(this).find("LI.disabled").removeClass("disabled");return ($(this));}$(this).each(function(){if(o!=undefined){var d=o.split(",");for(var i=0;i<d.length;i++){$(this).find("A[href=\""+d[i]+"\"]").parent().removeClass("disabled");}}});return ($(this));},disableContextMenu:function(){$(this).each(function(){$(this).addClass("disabled");});return ($(this));},enableContextMenu:function(){$(this).each(function(){$(this).removeClass("disabled");});return ($(this));},destroyContextMenu:function(){$(this).each(function(){$(this).unbind("mousedown").unbind("mouseup");});return ($(this));}});})(jQuery);}