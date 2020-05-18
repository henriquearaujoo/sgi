
function steping(params) {

	var myContainer = document.getElementsByClassName('tab-menu-item-vi');
	console.log(params);
	if (params >= 0) {
		for (var i = 0; i < myContainer.length; i++) {
			if (params === i) {
				myContainer.item(i).style.display = '';
			} else {
				myContainer.item(i).style.display = 'none';
			}

		}
	}

}

function renderedInfoRecurso() {
	document.getElementById("info-geral").style.display = 'none';
	document.getElementById("info-document").style.display = '';
	document.getElementById("info-recurso").style.display = 'none';
	document.getElementById("info-confirmacao").style.display = 'none';
}






	