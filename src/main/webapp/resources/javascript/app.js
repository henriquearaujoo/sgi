/**
 * 
 */$(function() {
		$('.js-toggle').bind('click', function(event) {
		$('.js-sidebar, .js-content').toggleClass('is-toggled');
		$('.js-toggle').toggleClass('option-menu');
		$('.js-menu').toggleClass('is-open-input-menu-search');
		
		this.style.color =  '#fff';
			
		var bar = $('#bar');
		
		bar.toggleClass('fa-bars');
		
		bar.toggleClass('fa-close');
		bar.toggleClass('ui-icon-close-i');
		
		event.preventDefault();
	});
});


$(function() {
	$('.js-menu-toggle').bind('click', function(event) {
		$('.js-profile-menu').toggleClass('profile-menu-active');
		event.preventDefault();
	});
});


$(document).on('focus','input',function() {
	
	//console.log('Focus');
	$(this).parents('.form-group').addClass('focused');
	
	
});

$(document).on('blur','input',function() {
	
	var inputValue = $(this).val();
	//console.log($(this));
	if (inputValue == "") {
//		$(this).removeClass('filled');
//		$(this).parents('.form-group').removeClass('focused');
//		console.log('Passou aqui');
	} else {
		$(this).addClass('filled');
		$(this).removeClass('ui-state-error');
	}	
});


function setarFocused(divPanel) {	
	divs = document.getElementById(divPanel).getElementsByClassName('form-group');
	
	for (var i = 0; i < divs.length; i++) {
		var input = divs[i].getElementsByClassName('ui-inputfield')[0];
		if (input != null && input.value != "") {
			divs[i].classList.add("focused");
			//console.log(input);
		}
	}
	
}
setarFocused("componenet-panel");
setarFocused("info-geral")

