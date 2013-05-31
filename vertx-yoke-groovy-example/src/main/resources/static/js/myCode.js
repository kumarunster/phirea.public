var showPageAsAjaxDialog = function(relativePath) {
	
	var $div = $('<div />').appendTo('body');
	$div.attr('id', 'showPageAsAjaxDialog');
	$div.attr('class', 'modal hide fade');
	$div.attr('tabindex', '-1');
	$div.attr('role', 'dialog');
	$div.attr('aria-labelledby', 'myModalLabel');
	$div.attr('aria-hidden', 'true');
	
	
	var $dialog = $div.load(relativePath);
	
	$dialog.modal('show');
	
	$dialog.on('hidden', function (divDialog) {
		console.log('hide the page as dialog: ' + relativePath + " dialogState: " + divDialog.__dialogState);
		if("showing" === divDialog.__dialogState) {
			divDialog.parent().remove();
			console.log("removed the dialog");
		}
	}($dialog));
	
	$dialog.on('show', function (divDialog) {
		divDialog.__dialogState = "showing";
		console.log('show the page as dialog: ' + relativePath);
	}($dialog)); 
	
}


function show_modal_large(href, callback) {
    // create guid to name of this Modal
    var randomNum = guid();
    // create element with guid id
    var elementName = '#' + randomNum.toString();
    $('body').append("<div id=" + randomNum + " class='modal hide fade in'></div>");
    // create Modal and show
    $.get(href, function (data) {
        $(elementName).html(data);
        // register call back to 'hidden' event
        $(elementName).on('hidden', function () {
            callback();
            // clean up after hidden
            $(elementName).unbind();
            $(elementName).remove();
        });
        $(elementName).modal('show');
    });
};

function s4() {
    return Math.floor((1 + Math.random()) * 0x10000)
               .toString(16)
               .substring(1);
};

function guid() {
    return s4() + s4() + '-' + s4() + '-' + s4() + '-' +
           s4() + '-' + s4() + s4() + s4();
}