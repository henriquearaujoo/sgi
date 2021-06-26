function start() {
	PF('statusDialog').show();
}

function stop() {
	PF('statusDialog').hide();
}


function setarWidthHeight(id) {
	var htmlTag = document.getElementById(id);
	htmlTag.style.height = Math.floor(window.innerHeight*0.98)+"px";	
	htmlTag.style.width = Math.floor(window.innerWidth*0.98)+"px";	
	
}

function minMaxWindow() {
	
}


function blockedScroll() {
	var ht = document.querySelector('html')
	ht.style.position = 'fixed'
	
	var dialog = document.getElementById('dialog_help');
	var titleDialog = dialog.firstElementChild
	var btnMin = titleDialog.lastElementChild
	btnMin.addEventListener('click', () => {
		if (ht.style.position === 'fixed') {
			unlockedScroll()	
		} else {
			ht.style.position = 'fixed'
		}
	})
}


function unlockedScroll() {
	var ht = document.querySelector('html')
	ht.style.position = null
}

PrimeFaces.locales ['pt'] = {
	    closeText: 'Fechar',
	    prevText: 'Anterior',
	    nextText: 'Próximo',
	    monthNames: ['Janeiro','Fevereiro','Março','Abril','Maio','Junho','Julho','Agosto','Setembro','Outubro','Novembro','Dezembro'],
	    monthNamesShort: ['Jan','Fev','Mar','Abr','Mai','Jun', 'Jul','Ago','Set','Out','Nov','Dez'],
	    dayNames: ['Domingo','Segunda','Terça','Quarta','Quinta','Sexta','Sábado'],
	    dayNamesShort: ['Dom','Seg','Ter','Qua','Qui','Sex','Sáb'],
	    dayNamesMin: ['D','S','T','Q','Q','S','S'],
	    weekHeader: 'Semana',
	    weekNumberTitle: 'Sm',
	    firstDay: 0,
	    isRTL: false,
	    showMonthAfterYear: false,
	    yearSuffix:'',
	    timeOnlyTitle: 'Só Horas',
	    timeText: 'Tempo',
	    hourText: 'Hora',
	    minuteText: 'Minuto',
	    secondText: 'Segundo',
	    currentText: 'Começo',
	    ampm: false,
	    month: 'Mês',
	    week: 'Semana',
	    day: 'Dia',
	    allDayText: 'Todo o Dia',
	    'MONTHS': ["Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"],
	    'MONTHS_SHORT': ["Jan", "Fev", "Mar", "Abr", "Mai", "Jun", "Jul", "Ago", "Set", "Out", "Nov", "Dez"],
	    'DAYS': ["Domingo", "Segunda-feira", "Terça-feira", "Quarta-feira", "Quinta-feira", "Sexta-feira", "Sábado"],
	    'DAYS_SHORT': ["Dom", "Seg", "Ter", "Qua", "Qui", "Sex", "Sáb"],
	    'ZOOM_IN': "Aumentar zoom",
	    'ZOOM_OUT': "Diminuir zoom",
	    'MOVE_LEFT': "Mover esquerda",
	    'MOVE_RIGHT': "Mover direita",
	    'NEW': "Novo",
	    'CREATE_NEW_EVENT': "Criar novo evento" };