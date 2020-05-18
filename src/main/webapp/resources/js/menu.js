
function findMenu(value) {

	var input, filter, ul, li, a, i;
	
	input = document.getElementById("menu-find");

	filter = value.toUpperCase();
	
	console.log(filter);
	
	ul = document.getElementById('panel-menu-p').getElementsByClassName('ui-panelmenu-panel');	
	
	for (var i = 0; i < ul.length; i++) {
		
		li = ul[i].getElementsByTagName("li");
		var contem = false;
		
		for (j = 0; j < li.length; j++) {
			
			a = li[j].getElementsByTagName("a")[0].getElementsByTagName('span')[0];
			
			if (a.innerHTML.toUpperCase().indexOf(filter) > -1) {
				li[j].style.display = "";
				contem = true;
			} else {
				li[j].style.display = "none";
			}			
		}
		
		if (contem) {
			ul[i].style.display = "";
		}else{
			ul[i].style.display = "none";
		}
	}

};

