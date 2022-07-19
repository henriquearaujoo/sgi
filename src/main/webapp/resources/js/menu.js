$(document).ready(main);



function main(){
	
	var uls = $('#menu ul');
	uls.hide();
	var contator = 0;
	
	$('#menu > li').click(function( e ){
		e.stopPropagation();
		uls.hide();	
		//$( this ).find('ul:first').css("display", "none");
		$( this ).find('ul:first').fadeIn(200);
		//$( this ).find('ul:first').show();
		
	});
	
	$('body').click(function(e){
		uls.hide();
	});


};
