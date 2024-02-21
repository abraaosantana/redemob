$(document).ready(function() {
	
	var showSpinner = function() {
		$('#spinner').modal({
			backdrop: 'static'
		})
	}

	var closeSpinner = function() {
		$('#spinner').modal('hide')
	}
	
	$(document).on("ajaxSend", showSpinner).on("ajaxStop", closeSpinner).on("ajaxError", closeSpinner)
});