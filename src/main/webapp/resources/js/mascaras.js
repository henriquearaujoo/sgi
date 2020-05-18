
function isEmail(email)
	{
	  er = /^[a-zA-Z0-9][a-zA-Z0-9\._-]+@([a-zA-Z0-9\._-]+\.)[a-zA-Z-0-9]{2}/;
	  
	  if(er.exec(email))
		{
		  return true;
		} else {
		  return false;
		}
	}

function validaEmail(email){
	
}

function validacaoEmail(field) {
	
	if (field.value === "") {
		return true;
	}
	
	
	usuario = field.value.substring(0, field.value.indexOf("@"));
	dominio = field.value.substring(field.value.indexOf("@") + 1,
			field.value.length);

	if ((usuario.length >= 1) && (dominio.length >= 3)
			&& (usuario.search("@") == -1) && (dominio.search("@") == -1)
			&& (usuario.search(" ") == -1) && (dominio.search(" ") == -1)
			&& (dominio.search(".") != -1) && (dominio.indexOf(".") >= 1)
			&& (dominio.lastIndexOf(".") < dominio.length - 1)) {
		//document.getElementById("msgemail").innerHTML = "E-mail válido";
		
		
		//alert("E-mail valido");
		
	} else {
		//document.getElementById("msgemail").innerHTML = "<font color='red'>E-mail inválido </font>";
		field.value = "";
		alert("E-mail invalido");
		
	}
}

function verificarCNPJ(cnpj) {

	if (!validarCNPJ(cnpj)) {
		cnpj.value = "";
		alert("CNPJ inválido");
		return false;
	}

	return true;

}

function verificarCPF(cpf) {

	if (!validarCPF(cpf)) {
		cpf.value = "";
		alert("CPF invalido");
		return false;
	}

	return true;

}

function validarCPF(Objcpf) {
	var cpf = Objcpf.value;
	exp = /\.|\-/g
	cpf = cpf.toString().replace(exp, "");
	var digitoDigitado = eval(cpf.charAt(9) + cpf.charAt(10));
	var soma1 = 0, soma2 = 0;
	var vlr = 11;

	for (i = 0; i < 9; i++) {
		soma1 += eval(cpf.charAt(i) * (vlr - 1));
		soma2 += eval(cpf.charAt(i) * vlr);
		vlr--;
	}
	soma1 = (((soma1 * 10) % 11) == 10 ? 0 : ((soma1 * 10) % 11));
	soma2 = (((soma2 + (2 * soma1)) * 10) % 11);

	var digitoGerado = (soma1 * 10) + soma2;
	if (digitoGerado != digitoDigitado) {
		Objcpf.value = "";
		alert('CPF Invalido!');
	}
}

function validarCNPJ(cnpjObject) {

	cnpj = cnpjObject.value;
	cnpj = cnpj.replace(/[^\d]+/g, '');

	if (cnpj == '')
		return false;

	if (cnpj.length != 14)
		return false;

	// Elimina CNPJs invalidos conhecidos
	if (cnpj == "00000000000000" || cnpj == "11111111111111"
			|| cnpj == "22222222222222" || cnpj == "33333333333333"
			|| cnpj == "44444444444444" || cnpj == "55555555555555"
			|| cnpj == "66666666666666" || cnpj == "77777777777777"
			|| cnpj == "88888888888888" || cnpj == "99999999999999")
		return false;

	// Valida DVs
	tamanho = cnpj.length - 2
	numeros = cnpj.substring(0, tamanho);
	digitos = cnpj.substring(tamanho);
	soma = 0;
	pos = tamanho - 7;
	for (i = tamanho; i >= 1; i--) {
		soma += numeros.charAt(tamanho - i) * pos--;
		if (pos < 2)
			pos = 9;
	}
	resultado = soma % 11 < 2 ? 0 : 11 - soma % 11;
	if (resultado != digitos.charAt(0))
		return false;

	tamanho = tamanho + 1;
	numeros = cnpj.substring(0, tamanho);
	soma = 0;
	pos = tamanho - 7;
	for (i = tamanho; i >= 1; i--) {
		soma += numeros.charAt(tamanho - i) * pos--;
		if (pos < 2)
			pos = 9;
	}
	resultado = soma % 11 < 2 ? 0 : 11 - soma % 11;
	if (resultado != digitos.charAt(1))
		return false;

	return true;

}

// Permite somente n�meros
function somenteNumeros(numero) {

	if (document.all) { // Internet Explorer
		tecla = numero.keyCode;
	} else if (document.getElementById) { // Nestcape/FireFox
		tecla = numero.which;
	}

	// tecla==8 � para permitir o backspace funcionar para apagar
	if ((tecla >= 48 && tecla <= 57) || tecla == 8 || tecla == 0) {
		return true;
	} else {
		return false;
	}
}

function mascaraTelefone(o, f) {
	v_obj = o
	v_fun = f
	setTimeout("execmascara()", 1)
}

function mascaraMoeda(o, f) {
	v_obj = o
	v_fun = f
	setTimeout("execmascara()", 1)
}

function moeda(v) {
	v = v.replace(/\D/g, ""); // permite digitar apenas numero
	v = v.replace(/(\d{1})(\d{15})$/, "$1.$2") // coloca ponto antes dos
	// ultimos digitos
	v = v.replace(/(\d{1})(\d{11})$/, "$1.$2") // coloca ponto antes dos
	// ultimos 11 digitos
	v = v.replace(/(\d{1})(\d{8})$/, "$1.$2") // coloca ponto antes dos
	// ultimos 8 digitos
	v = v.replace(/(\d{1})(\d{5})$/, "$1.$2") // coloca ponto antes dos
	// ultimos 5 digitos
	v = v.replace(/(\d{1})(\d{1,2})$/, "$1,$2") // coloca virgula antes dos
	// ultimos 2 digitos
	return v;
}

function execmascara() {
	v_obj.value = v_fun(v_obj.value)
}

function mtel(v) {
	v = v.replace(/\D/g, ""); // Remove tudo o que n�o � d�gito
	v = v.replace(/^(\d{2})(\d)/g, "($1) $2"); // Coloca par�nteses em volta
	// dos dois primeiros d�gitos
	v = v.replace(/(\d)(\d{4})$/, "$1-$2"); // Coloca h�fen entre o quarto e o
	// quinto d�gitos
	return v;
}

function id(el) {
	return document.getElementById(el);
}

function formatarNumero(ObjForm, teclapres, tammax, decimais) {
	var bksp = 8;
	var key_0 = 48;
	var key_9 = 57;

	if (document.all) { // Internet Explorer
		tecla = teclapres.keyCode;
	} else if (document.getElementById) { // Nestcape
		tecla = teclapres.which;
	}

	var tamanhoObjeto = ObjForm.value.length;

	if (tecla == bksp && tamanhoObjeto == tammax) {
		tamanhoObjeto = tamanhoObjeto - 1;
	}

	if (tecla == bksp
			|| (tecla >= key_0 && tecla <= key_9 && (tamanhoObjeto + 1) <= tammax))
	// if (( tecla == bksp || tecla == 88 || tecla >= 48 && tecla <= 57 || tecla
	// >= 96 && tecla <= 105 ) && ((tamanhoObjeto+1) <= tammax))
	{
		vr = ObjForm.value;
		vr = vr.replace(eval("/\\./g"), "")
		vr = vr.replace(",", "");
		tam = vr.length;

		if (tam < tammax && tecla != bksp) {
			tam = vr.length + 1;
		}

		if ((tecla == bksp) && (tam > 1)) {
			tam = tam - 1;
			vr = ObjForm.value;

			vr = vr.replace(eval("/\\./g"), "")
			vr = vr.replace(",", "");
		}

		// C�lculo para casas decimais setadas por parametro
		if (tecla == bksp || (tecla >= key_0 && tecla <= key_9)) {
			if (decimais > 0) {

				if ((tam <= decimais)) {
					ObjForm.value = ("0," + vr);
				}
				if ((tam == (decimais + 1)) && (tecla == 8)) {
					ObjForm.value = vr.substr(0, (tam - decimais)) + ','
							+ vr.substr(tam - (decimais), tam);
				}
				if ((tam > (decimais + 1)) && (tam <= (decimais + 3))
						&& ((vr.substr(0, 1)) == "0")) {
					ObjForm.value = vr.substr(1, (tam - (decimais + 1))) + ','
							+ vr.substr(tam - (decimais), tam);
				}
				if ((tam > (decimais + 1)) && (tam <= (decimais + 3))
						&& ((vr.substr(0, 1)) != "0")) {
					ObjForm.value = vr.substr(0, tam - decimais) + ','
							+ vr.substr(tam - decimais, tam);
				}
				if ((tam >= (decimais + 4)) && (tam <= (decimais + 6))) {
					ObjForm.value = vr.substr(0, tam - (decimais + 3)) + '.'
							+ vr.substr(tam - (decimais + 3), 3) + ','
							+ vr.substr(tam - decimais, tam);
				}
				if ((tam >= (decimais + 7)) && (tam <= (decimais + 9))) {
					// ObjForm.value = vr.substr( 0, tam - (decimais + 6) ) +
					// '.' + vr.substr( tam - (decimais + 6), 3 ) + '.' +
					// vr.substr( tam - (decimais + 3), 3 ) + ',' + vr.substr(
					// tam - decimais, tam ) ;
					teclapres.cancelBubble = true;
					return false;
				}
				if ((tam >= (decimais + 10)) && (tam <= (decimais + 12))) {
					ObjForm.value = vr.substr(0, tam - (decimais + 9)) + '.'
							+ vr.substr(tam - (decimais + 9), 3) + '.'
							+ vr.substr(tam - (decimais + 6), 3) + '.'
							+ vr.substr(tam - (decimais + 3), 3) + ','
							+ vr.substr(tam - decimais, tam);
				}
				if ((tam >= (decimais + 13)) && (tam <= (decimais + 15))) {
					ObjForm.value = vr.substr(0, tam - (decimais + 12)) + '.'
							+ vr.substr(tam - (decimais + 12), 3) + '.'
							+ vr.substr(tam - (decimais + 9), 3) + '.'
							+ vr.substr(tam - (decimais + 6), 3) + '.'
							+ vr.substr(tam - (decimais + 3), 3) + ','
							+ vr.substr(tam - decimais, tam);
				}
			} else if (decimais == 0) {
				if (tam <= 3) {
					ObjForm.value = vr;
				}
				if ((tam >= 4) && (tam <= 6)) {
					if (tecla == bksp) {
						ObjForm.value = vr.substr(0, tam);
						teclapres.cancelBubble = true;
						return false;
					}

					ObjForm.value = vr.substr(0, tam - 3) + '.'
							+ vr.substr(tam - 3, 3);
				}
				if ((tam >= 7) && (tam <= 9)) {
					if (tecla == bksp) {
						ObjForm.value = vr.substr(0, tam);
						teclapres.cancelBubble = true;
						return false;
					}
					ObjForm.value = vr.substr(0, tam - 6) + '.'
							+ vr.substr(tam - 6, 3) + '.'
							+ vr.substr(tam - 3, 3);
				}
				if ((tam >= 10) && (tam <= 12)) {
					if (tecla == bksp) {
						ObjForm.value = vr.substr(0, tam);
						teclapres.cancelBubble = true;
						return false;
					}
					ObjForm.value = vr.substr(0, tam - 9) + '.'
							+ vr.substr(tam - 9, 3) + '.'
							+ vr.substr(tam - 6, 3) + '.'
							+ vr.substr(tam - 3, 3);
				}
				if ((tam >= 13) && (tam <= 15)) {
					if (tecla == bksp) {
						ObjForm.value = vr.substr(0, tam);
						teclapres.cancelBubble = true;
						return false;
					}
					ObjForm.value = vr.substr(0, tam - 12) + '.'
							+ vr.substr(tam - 12, 3) + '.'
							+ vr.substr(tam - 9, 3) + '.'
							+ vr.substr(tam - 6, 3) + '.'
							+ vr.substr(tam - 3, 3);
				}
			}
		}
	} else {
		if ((tecla != 8) && (tecla != 9) && (tecla != 0) && (tecla != 13)
				&& (tecla != 35) && (tecla != 36) && (tecla != 46)) {
			teclapres.cancelBubble = true;
			return false;
		}
	}
}

function validacpf(theElement) {

	var i;

	s = theElement.value;

	if (s == "") {
		return true;
	}

	var c = s.substr(0, 9);

	var dv = s.substr(9, 2);

	var d1 = 0;

	for (i = 0; i < 9; i++)

	{

		d1 += c.charAt(i) * (10 - i);

	}

	if (d1 == 0) {

		alert("CPF Inválido. ");
		theElement.focus();

		return false;

	}

	d1 = 11 - (d1 % 11);

	if (d1 > 9)
		d1 = 0;

	if (dv.charAt(0) != d1)

	{

		alert("CPF Inválido. ");
		theElement.focus();

		return false;

	}

	d1 *= 2;

	for (i = 0; i < 9; i++)

	{

		d1 += c.charAt(i) * (11 - i);

	}

	d1 = 11 - (d1 % 11);

	if (d1 > 9)
		d1 = 0;

	if (dv.charAt(1) != d1)

	{

		alert("CPF Inválido. ");
		theElement.focus();

		return false;

	}

	return true;

}
